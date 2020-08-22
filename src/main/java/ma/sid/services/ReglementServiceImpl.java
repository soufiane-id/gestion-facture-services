package ma.sid.services;

import ma.sid.dao.*;
import ma.sid.dto.ChargeFixe;
import ma.sid.dto.DifferenceReglement;
import ma.sid.dto.TransfertArgent;
import ma.sid.dto.enums.StatutOperation;
import ma.sid.dto.enums.TypeOperation;
import ma.sid.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ReglementServiceImpl implements ReglementService{

    @Autowired
    private EcheancierRepository echeancierRepository;
    @Autowired
    private OperationBancaireRepository operationBancaireRepository;
    @Autowired
    private BanqueRepository banqueRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private SocieteRepository societeRepository;
    @Autowired
    private PaiementRepository paiementRepository;

    @Override
    public void reglerOperationsParCheque(List<Echeancier> echeanciers, Long codeOperation, BigDecimal montantFacture, BigDecimal montantFactureSelectionne) {
        OperationBancaire operationBancaire = getOperationFromCode(codeOperation);
        regler(echeanciers, montantFacture, montantFactureSelectionne, operationBancaire);
    }

    @Override
    public void reglerOperationBancaireParVirement(List<Echeancier> echeanciers, Long codeOperation, BigDecimal montantFactureSelectionne) {
        OperationBancaire operationBancaire = getOperationFromCode(codeOperation);
        BigDecimal montantOperation = getMontantFactureFromOperation(operationBancaire).abs();
        regler(echeanciers, montantOperation, montantFactureSelectionne, operationBancaire);
    }

    @Override
    public void reglerTransfertArgent(TransfertArgent transfertArgent) {
        this.transfererArgent(transfertArgent);
    }

    @Override
    public void reglerPaiementSansFacture(DifferenceReglement differenceReglement) {
        Personne client = personneRepository.getOne(differenceReglement.getIdClient());
        Societe societe = societeRepository.getOne(differenceReglement.getIdSociete());
        BigDecimal montantOperation = differenceReglement.getMontantFacture();

        creerFactureAvecExcedent(societe, client, montantOperation, differenceReglement.getOperationBancaire().getDateOperation());

        tracerPaiementEtVerserCompte(societe.getBanque().getIdBanque(), TypeOperation.CREDIT, montantOperation,
                client, differenceReglement.getOperationBancaire().getDateOperation(), differenceReglement.getIdSociete());
        //verserCompteSociete(societe.getBanque().getIdBanque(), TypeOperation.CREDIT, montantOperation);

        //On annule le montant pour que pour la prochaine fois qu'on regle cette opération, on ne reinsere pas le montant dans la banque.
        //differenceReglement.getOperationBancaire().setMontantCredit(BigDecimal.ZERO);

        validerOperation(differenceReglement.getOperationBancaire(), StatutOperation.EN_ATTENTE_FACTURE);
    }

    @Override
    public void reglerChargesFixes(ChargeFixe chargeFixe) {
        Societe societe = societeRepository.getOne(chargeFixe.getIdSociete());
        OperationBancaire operationBancaire = chargeFixe.getOperationBancaire();
        BigDecimal montantOperation = chargeFixe.getMontant();

        tracerPaiementEtVerserCompte(societe.getBanque().getIdBanque(), TypeOperation.DEBIT, montantOperation,
                null, operationBancaire.getDateOperation(), chargeFixe.getIdSociete());
        //verserCompteSociete(societe.getBanque().getIdBanque(), TypeOperation.DEBIT, montantOperation);

        validerOperation(operationBancaire, operationBancaire.getStatutOperation());
    }

    @Override
    public void reglerImpaye(List<Echeancier> echeanciers, Long codeOperation) {
        for(Echeancier echeancier : echeanciers) {
            Echeancier impaye = new Echeancier(null, echeancier.getDateFacture(), echeancier.getDateEcheance(), echeancier.getNumeroDocument() + "-IMPAYE-",
                    echeancier.getPersonne(), null, null, null, echeancier.getMontantFacture(),
                    BigDecimal.ZERO, echeancier.getMontantFacture(), null, echeancier.getSociete());

            OperationBancaire operationBancaire = operationBancaireRepository.getOne(codeOperation);

            // On trace le paiement avec le montant négatif
            tracerPaiement(echeancier.getPersonne(), operationBancaire.getDateOperation(), echeancier.getSociete().getIdSociete(), echeancier.getMontantPaye().negate());
            verserCompteSociete(echeancier.getSociete().getBanque().getIdBanque(), TypeOperation.DEBIT, echeancier.getMontantPaye());

            validerOperation(operationBancaire, StatutOperation.IMPAYE);

            echeancierRepository.save(impaye);
        }
    }

    /**
     * Règle une operation Bancaire en fonction du montant de la facture par rapport au montant Total Selectionne.
     */
    private synchronized void regler(List<Echeancier> echeanciers, BigDecimal montantFacture, BigDecimal montantFactureSelectionne,
                        OperationBancaire operationBancaire){
        ajouterOperationALEcheance(echeanciers, operationBancaire);
        if(montantFacture.compareTo(montantFactureSelectionne) < 0) {
            reglementPartiel(echeanciers, montantFacture, operationBancaire);
        }else if(montantFacture.compareTo(montantFactureSelectionne) == 0) {
            reglementExact(echeanciers, operationBancaire);
        }else {
            reglementAvecExcedent(echeanciers, montantFacture, operationBancaire);
        }
        validerOperation(operationBancaire, StatutOperation.VALIDE);
    }

    private void ajouterOperationALEcheance(List<Echeancier> echeanciers, OperationBancaire operationBancaire) {
        for(Echeancier e : echeanciers) {
            if(e.getOperations() != null && !e.getOperations().isEmpty()) {
                Set<Long> existant = e.getOperations();
                existant.add(operationBancaire.getCodeOperation());
                e.setOperations(existant);
            }else {
                List<Long> opToAdd = Arrays.asList(operationBancaire.getCodeOperation());
                e.setOperations(new HashSet<>(opToAdd));
            }
        }
    }

    private synchronized void reglementAvecExcedent(List<Echeancier> echeanciers, BigDecimal montantFacture,
                                       OperationBancaire operationBancaire) {
        BigDecimal montantInitialAVerser = montantFacture.add(BigDecimal.ZERO); // On clone montantFacture
        BigDecimal sommeFactures = echeanciers.stream().map(e -> e.getResteAPayer()).reduce(BigDecimal.ZERO, BigDecimal::add);
        for(int i = 0; i < echeanciers.size(); i++) {
            montantFacture = montantFacture.subtract(echeanciers.get(i).getResteAPayer());
            reglementExact(Arrays.asList(echeanciers.get(i)), operationBancaire);
            // Si c'est le dernier element
            if(i == (echeanciers.size()-1) ) {
                // On verse aussi l'excedent à la banque.
                tracerPaiementEtVerserCompte(echeanciers.get(i).getSociete().getBanque().getIdBanque(), operationBancaire.getTypeOperation(), montantFacture,
                        echeanciers.get(i).getPersonne(), operationBancaire.getDateOperation(), echeanciers.get(i).getSociete().getIdSociete());
                //verserCompteSociete(echeanciers.get(i).getSociete().getBanque().getIdBanque(), typeOperation, montantFacture);
                creerFactureAvecExcedent(echeanciers.get(i).getSociete(), echeanciers.get(i).getPersonne(), montantInitialAVerser.subtract(sommeFactures), operationBancaire.getDateOperation());
            }
        }

    }

    private synchronized void reglementExact(List<Echeancier> echeanciers, OperationBancaire operationBancaire) {
        for (Echeancier echeance : echeanciers) {
            tracerPaiementEtVerserCompte(echeance.getSociete().getBanque().getIdBanque(), operationBancaire.getTypeOperation(), echeance.getResteAPayer(),
                    echeance.getPersonne(), operationBancaire.getDateOperation(), echeance.getSociete().getIdSociete());
            //verserCompteSociete(echeance.getSociete().getBanque().getIdBanque(), operationBancaire.getTypeOperation(), echeance.getResteAPayer());
            echeance.setMontantPaye(echeance.getMontantFacture());
            echeance.setResteAPayer(BigDecimal.ZERO);
            echeancierRepository.save(echeance);
        }

    }

    private synchronized void reglementPartiel(List<Echeancier> echeanciers, BigDecimal montantFacture,
                                  OperationBancaire operationBancaire) {
        echeanciers.sort(Comparator.comparing(Echeancier::getDateFacture));
        for(Echeancier echeance : echeanciers) {
            if(montantFacture.compareTo(echeance.getResteAPayer()) > 0) {
                montantFacture = montantFacture.subtract(echeance.getResteAPayer());
                reglementExact(Arrays.asList(echeance), operationBancaire);
            }else {
                echeance.setMontantPaye(montantFacture.add(echeance.getMontantPaye()));
                echeance.setResteAPayer(echeance.getResteAPayer().subtract(montantFacture));
                echeancierRepository.save(echeance);
                tracerPaiementEtVerserCompte(echeance.getSociete().getBanque().getIdBanque(), operationBancaire.getTypeOperation(), montantFacture,
                        echeance.getPersonne(), operationBancaire.getDateOperation(), echeance.getSociete().getIdSociete());
                //verserCompteSociete(echeance.getSociete().getBanque().getIdBanque(), operationBancaire.getTypeOperation(), montantFacture);
                return;
            }
        }

    }

    private OperationBancaire getOperationFromCode(Long codeOPeration) {
        OperationBancaire operation =  operationBancaireRepository.getOne(codeOPeration);
        // On valorise le type de l'opération
        if(operation.getMontantDebit() != null) {
            operation.setTypeOperation(TypeOperation.DEBIT);
        }else {
            operation.setTypeOperation(TypeOperation.CREDIT);
        }
        return operation;
    }

    private BigDecimal getMontantFactureFromOperation(OperationBancaire operation) {
        return operation.getMontantDebit() != null ? operation.getMontantDebit() : operation.getMontantCredit();
    }

    private void validerOperation(OperationBancaire operationBancaire, StatutOperation statutOperation) {
        operationBancaire.setStatutOperation(statutOperation);
        operationBancaireRepository.save(operationBancaire);
    }

    private synchronized void tracerPaiementEtVerserCompte(Long codeBanque, TypeOperation typeOperation, BigDecimal montantAVerser,
                                                           Personne personne, Date datePaiement, Long idSociete) {
        tracerPaiement(personne, datePaiement, idSociete, montantAVerser);
        verserCompteSociete(codeBanque, typeOperation, montantAVerser);

    }

    private synchronized void tracerPaiement(Personne personne, Date datePaiement, Long idSociete, BigDecimal montantAVerser) {
        Societe societe = societeRepository.getOne(idSociete);
        Paiement paiement = new Paiement(null, personne, societe, montantAVerser, datePaiement);
        paiementRepository.save(paiement);
    }

    private synchronized void verserCompteSociete(Long codeBanque, TypeOperation typeOperation, BigDecimal montantAVerser) {
        Banque banque = banqueRepository.getOne(codeBanque);
        if(TypeOperation.CREDIT.equals(typeOperation)) {
            BigDecimal montantExistant = banque.getSoldeCredit();
            BigDecimal nouveauMontant = montantAVerser.add(montantExistant);
            banque.setSoldeCredit(nouveauMontant);
        }else if(TypeOperation.DEBIT.equals(typeOperation)) {
            BigDecimal montantExistant = banque.getSoldeDebit();
            BigDecimal nouveauMontant = montantAVerser.add(montantExistant);
            banque.setSoldeDebit(nouveauMontant);
        }
        banqueRepository.save(banque);
    }

    private synchronized void transfererArgent(TransfertArgent transfertArgent) {
        OperationBancaire operation = operationBancaireRepository.getOne(transfertArgent.getCodeOperation());
        Banque banqueFrom = banqueRepository.getOne(transfertArgent.getBanqueFrom());
        Banque banqueTo = banqueRepository.getOne(transfertArgent.getBanqueTo());
        BigDecimal montantTransaction = transfertArgent.getMontant();

        banqueFrom.setSoldeDebit(banqueFrom.getSoldeDebit().add(montantTransaction));
        banqueTo.setSoldeCredit(banqueTo.getSoldeCredit().add(montantTransaction));

        banqueRepository.save(banqueFrom);
        banqueRepository.save(banqueTo);

        validerOperation(operation, StatutOperation.TRANSFERE);
    }

    private synchronized void creerFactureAvecExcedent(Societe societe, Personne personne, BigDecimal montantExcedent, Date dateEcheance) {
        Echeancier excedent = new Echeancier(null, dateEcheance, null, "EXCEDENT",
                personne, personne.getNomPersonne(), null, null, null, montantExcedent.negate(),
                BigDecimal.ZERO, montantExcedent.negate(), null, societe);

        echeancierRepository.save(excedent);
    }
}
