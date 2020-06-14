package ma.sid.services;

import ma.sid.dao.EcheancierRepository;
import ma.sid.dao.OperationBancaireRepository;
import ma.sid.dao.PersonneRepository;
import ma.sid.dto.enums.StatutIntegration;
import ma.sid.dto.enums.TypePersonne;
import ma.sid.entities.*;
import ma.sid.exceptions.NonUniqueDocumentException;
import ma.sid.exceptions.PersonneNotFoundException;
import ma.sid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EcheancierServiceImpl implements EcheancierService{

    @Autowired
    private EcheancierRepository echeancierRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private OperationBancaireRepository operationBancaireRepository;

    @Override
    public List<Echeancier> findAllEcheancesClient() {
        return echeancierRepository.findAllEcheancesClient();
    }

    @Override
    public List<Echeancier> findAllEcheancesClientNonRegles() {
        List<Echeancier> result = echeancierRepository.findAllEcheancesClientNonRegles();
        Collections.sort(result);
        return result;
    }

    @Override
    public List<Echeancier> findAllEcheancesClientRegles() {
        return echeancierRepository.findAllEcheancesClientRegles();
    }

    @Override
    public List<Echeancier> findAllEcheancesFournisseurNonRegles() {
        List<Echeancier> result = echeancierRepository.findAllEcheancesFournisseurNonRegles();
        Collections.sort(result);
        return result;
    }

    @Override
    public List<Echeancier> findAllEcheancesFournisseurRegles() {
        return echeancierRepository.findAllEcheancesFournisseurRegles();
    }

    @Override
    public List<Echeancier> findAllEcheancesRegles() {
        return echeancierRepository.findAllEcheancesRegles();
    }

    @Override
    public Echeancier ajouterEcheancier(Echeancier echeancier) {
        // Lors de l'ajout, on force le RAP au montant de la facture, en attendant le réglement de l'echeance.
        echeancier.setResteAPayer(echeancier.getMontantFacture());

        if(echeancier.getMontantPaye() == null) {
            echeancier.setMontantPaye(BigDecimal.ZERO);
        }

        // On valorise la personne (Intégration Excel)
        if(echeancier.getPersonne() == null) {
            Personne personne = findPersonneByName(echeancier.getNomPersonne().toUpperCase().trim());
            if(personne == null) {
                throw new PersonneNotFoundException("Client inconnu");
            }
            echeancier.setPersonne(personne);
        } else {
            if(!isPersonneValid(echeancier.getPersonne().getNomPersonne())){
                throw new PersonneNotFoundException("Client inconnu");
            }
        }

        remplirDateEcheance(echeancier);

        return echeancierRepository.save(echeancier);
    }

    @Override
    public List<Echeancier> integrerEcheanciers(List<Echeancier> echeanciers) {
        for(Echeancier echeance : echeanciers) {
            try {
                ajouterEcheancier(echeance);
                echeance.setStatutIntegration(StatutIntegration.OK);
            } catch (Exception e) {
                echeance.setStatutIntegration(StatutIntegration.ERREUR);
            }
        }

        return echeanciers;
    }

    @Override
    public List<Echeancier> getEcheanciersByPersonne(Long idPersonne, Long idSociete) {
        List<Echeancier> result = echeancierRepository.getEcheanciersByPersonne(idPersonne, idSociete);
        Collections.sort(result);
        return result;
    }

    @Override
    public Set<Echeancier> recupererEcheanciersParMontants(List<BigDecimal> montants, Long idPersonne) {
        Set<Echeancier> resultat = new HashSet<>();
        for(BigDecimal montant: montants) {
            resultat.addAll(findEcheancierByMontant(montant, idPersonne));
        }
        return resultat;
    }

    @Override
    public List<Echeancier> recupererEcheancesParOperation(Long operationBancaireId) {
        OperationBancaire operationBancaire = operationBancaireRepository.getOne(operationBancaireId);
        Set<Long> operation = new HashSet<>();
        operation.add(operationBancaireId);
        return echeancierRepository.findAllByOperations(operation);
    }

    @Override
    public BigDecimal recupererCreancesClients() {
        return echeancierRepository.findAllEcheancesClientNonRegles().stream()
                .map(echeancier -> echeancier.getResteAPayer())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal recupererDettesFrs() {
        return echeancierRepository.findAllEcheancesFournisseurNonRegles().stream()
                .map(echeancier -> echeancier.getResteAPayer())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Echeancier recupererEcheanciersParNumeroDocument(String numeroDocument) {
        List<Echeancier> echeanciers = echeancierRepository.findByNumeroDocument(numeroDocument);
        if(echeanciers == null || echeanciers.isEmpty()) {
            throw new NonUniqueDocumentException("Aucune facture trouvée. Veuillez réessayer");
        }
        if(echeanciers.size() > 1){
            throw new NonUniqueDocumentException("Plusieurs documents correspondent à votre requête. Veuillez réessayer");
        }
        return echeanciers.get(0);
    }

    @Override
    public BigDecimal recupererAchatsClientParDate(Personne client, Date startWeek, Date endWeek) {
        List<Echeancier> echeanciers = echeancierRepository.findAllByPersonneAndDateFactureGreaterThanEqualAndDateFactureLessThanEqual(client, startWeek, endWeek);
        return echeanciers.stream().map(e -> e.getResteAPayer()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Personne findPersonneByName(String nomPersonne) {
        if(nomPersonne != null) {
            Personne personne = new Personne();
            personne.setNomPersonne(nomPersonne);
            Example<Personne> personneExample = Example.of(personne);
            Iterable<Personne> personnes = personneRepository.findAll(personneExample);
            for (Personne p : personnes) {
                return p;
            }
        }
        return null;
    }
    private List<Echeancier> findEcheancierByMontant(BigDecimal montant, Long idPersonne) {
        List<Echeancier> result = null;
        Personne personne = new Personne();
        personne.setIdPersonne(idPersonne);
        Echeancier criteria = new Echeancier();
        criteria.setResteAPayer(montant);
        criteria.setPersonne(personne);
        Example<Echeancier> echeancierExample = Example.of(criteria);
        Iterable<Echeancier> echeanciers = echeancierRepository.findAll(echeancierExample);
        /*for (Echeancier e : echeanciers) {
            return e;
        }*/
        return StreamSupport.stream(echeanciers.spliterator(), false)
                .collect(Collectors.toList());
    }

    private Boolean isPersonneValid(String nomPersonne) {
        Personne p = personneRepository.findByNomPersonne(nomPersonne.toUpperCase().trim());
        return p != null ? Boolean.TRUE : Boolean.FALSE;
    }

    private void remplirDateEcheance(Echeancier echeancier) {
        if(TypePersonne.CLIENT.equals(echeancier.getPersonne().getTypePersonne())){
            if(echeancier.getDateEcheance() == null) {
                echeancier.setDateEcheance(DateUtils.addDays(echeancier.getDateFacture(), 20));
            }
        } else if(TypePersonne.FOURNISSEUR.equals(echeancier.getPersonne().getTypePersonne())) {
            String nomFournisseur = echeancier.getPersonne().getNomPersonne().toUpperCase().trim();
            switch (nomFournisseur) {
                case "TENDRIADE":
                case "LORNOY":
                case "DGL":
                case "SFD":
                case "AVS":
                    if(echeancier.getDateEcheance() == null) {
                        echeancier.setDateEcheance(DateUtils.addDays(echeancier.getDateFacture(), 30));
                    }
                    break;
                case "ICM":
                    if(echeancier.getDateEcheance() == null) {
                        echeancier.setDateEcheance(DateUtils.addDays(echeancier.getDateFacture(), 8));
                    }
                    break;
                default:
                    if(echeancier.getDateEcheance() == null) {
                        echeancier.setDateEcheance(DateUtils.addDays(echeancier.getDateFacture(), 20));
                    }
                    break;
            }
        }
    }
}
