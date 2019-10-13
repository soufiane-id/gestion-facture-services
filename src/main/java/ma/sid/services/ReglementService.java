package ma.sid.services;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.BanqueRepository;
import ma.sid.dao.EcheancierClientRepository;
import ma.sid.dao.OperationBancaireRepository;
import ma.sid.dao.ReglementRepository;
import ma.sid.entities.Banque;
import ma.sid.entities.EcheancierClient;
import ma.sid.entities.MoyenPaiement;
import ma.sid.entities.OperationBancaire;
import ma.sid.entities.Reglement;
import ma.sid.entities.StatutOperation;
import ma.sid.entities.TypeOperation;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class ReglementService {

	@Autowired
	private ReglementRepository reglementRepository;
	@Autowired
	private EcheancierClientRepository echeancierClientRepository;
	@Autowired
	private OperationBancaireRepository operationBancaireRepository;
	@Autowired
	private BanqueRepository banqueRepository;
	
	@GetMapping("/listReglement")
    List<Reglement> findAll() {
        return reglementRepository.findAll();
    }
	
	@PostMapping("/reglerOperationsCheque/{codeOperation}/{montantFacture}/{montantFactureSelectionne}")
	public void reglerOperationsBancairesParCheque(@RequestBody List<EcheancierClient> echeanciers, @PathVariable Long codeOperation, @PathVariable BigDecimal montantFacture, @PathVariable BigDecimal montantFactureSelectionne) {
		OperationBancaire operationBancaire = getOperationFromCode(codeOperation);
		if(montantFacture.compareTo(montantFactureSelectionne) < 0) {
			reglementPartiel(echeanciers, montantFacture, operationBancaire.getTypeOperation());
		}else if(montantFacture.compareTo(montantFactureSelectionne) == 0) {
			reglementExact(echeanciers, montantFacture, operationBancaire.getTypeOperation());
		}else {
			reglementAvecExcedent(echeanciers, montantFacture, operationBancaire.getTypeOperation());
		}
		validerOperation(operationBancaire);
	}
	
	@PostMapping("/reglerOperations/{codeOperation}/{montantFactureSelectionne}")
	public void reglerOperationsBancaires(@RequestBody List<EcheancierClient> echeanciers, @PathVariable Long codeOperation, @PathVariable BigDecimal montantFactureSelectionne) {
		OperationBancaire operationBancaire = getOperationFromCode(codeOperation);
		BigDecimal montantFacture = getMontantFactureFromOperation(operationBancaire);
		if(montantFacture.compareTo(montantFactureSelectionne) < 0) {
			reglementPartiel(echeanciers, montantFacture, operationBancaire.getTypeOperation());
		}else if(montantFacture.compareTo(montantFactureSelectionne) == 0) {
			reglementExact(echeanciers, montantFacture, operationBancaire.getTypeOperation());
		}else {
			reglementAvecExcedent(echeanciers, montantFacture, operationBancaire.getTypeOperation());
		}
		validerOperation(operationBancaire);
	}

    private void reglementAvecExcedent(List<EcheancierClient> echeanciers, BigDecimal montantFacture,
			TypeOperation typeOperation) {
    	for(int i = 0; i < echeanciers.size(); i++) {
    		reglementExact(Arrays.asList(echeanciers.get(i)), montantFacture, typeOperation);
    		montantFacture = montantFacture.subtract(echeanciers.get(i).getMontantFacture());
    		// Si c'est le dernier element
    		if(i == (echeanciers.size()-1) ) {
    			// On verse aussi l'excedent à la banque.
    			verserCompteSociete(echeanciers.get(i), typeOperation, montantFacture);
    			// TODO A confirmer pour la date decheance + numero document + et les autres champs !
    			echeancierClientRepository.save(new EcheancierClient(null, new Date(), null, "EXCEDENT", echeanciers.get(i).getClient(), echeanciers.get(i).getCodeTiers(), null, null, montantFacture.negate(), BigDecimal.ZERO, montantFacture.negate(), new Date(), echeanciers.get(i).getSociete()));
    		}
    	}
    	
	}

	private void reglementExact(List<EcheancierClient> echeanciers, BigDecimal montantFacture,
			TypeOperation typeOperation) {
		for (EcheancierClient echeance : echeanciers) {
			echeance.setMontantPaye(echeance.getMontantFacture());
			echeance.setResteAPayer(BigDecimal.ZERO);
			echeance.setMoyenPaiement(MoyenPaiement.VIREMENT);
			//montantFacture = montantFacture.subtract(echeance.getMontantFacture());
			echeancierClientRepository.save(echeance);
			verserCompteSociete(echeance, typeOperation, echeance.getMontantFacture());
		}

	}

	private void reglementPartiel(List<EcheancierClient> echeanciers, BigDecimal montantFacture,
    		TypeOperation typeOperation) {
    	for(EcheancierClient echeance : echeanciers) {
    		if(montantFacture.compareTo(echeance.getMontantFacture()) > 0) {
    			reglementExact(Arrays.asList(echeance), montantFacture, typeOperation);
    			montantFacture = montantFacture.subtract(echeance.getMontantFacture());
    		}else {
    			//echeance.setMontantPaye(montantFacture);
    			echeance.setMontantPaye(montantFacture.add(echeance.getMontantPaye()));
    			//echeance.setResteAPayer(echeance.getMontantFacture().subtract(montantFacture));
    			echeance.setResteAPayer(echeance.getResteAPayer().subtract(montantFacture));
    			echeancierClientRepository.save(echeance);
    			verserCompteSociete(echeance, typeOperation, montantFacture);
    			return;
    		}
		}
	
    }
    
    public OperationBancaire getOperationFromCode(Long codeOPeration) {
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
    
    private void validerOperation(OperationBancaire operationBancaire) {
    	operationBancaire.setStatutOperation(StatutOperation.VALIDE);
		operationBancaireRepository.save(operationBancaire);
    }
    
    private void verserCompteSociete(EcheancierClient echeance, TypeOperation typeOperation, BigDecimal montantAVerser) {
    	Banque banque = banqueRepository.getOne(echeance.getSociete().getBanque().getIdBanque());
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
}
