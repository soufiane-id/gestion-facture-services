package ma.sid.services;

import java.math.BigDecimal;
import java.util.Arrays;
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
	
//	@PostMapping()
//	OperationBancaire reglerOperation(OperationBancaire operation) {
//		if(StatutOperation.CREE.equals(operation.getStatutOperation())) {
//			operation.setStatutOperation(StatutOperation.EN_COURS);
//			reglementRepository.save(new Reglement(null, null, operation.getMontantCredit() > 0 ? operation.getMontantCredit() : operation.getMontantDebit() , operation.get, fournisseur, societe))
//		}
//	}
	
	@PostMapping("/reglerOperations/{codeOperation}/{montantFactureSelectionne}")
	public void reglerOperationsBancaires(@RequestBody List<EcheancierClient> echeanciers, @PathVariable Long codeOperation, @PathVariable BigDecimal montantFactureSelectionne) {
		OperationBancaire operationBancaire = getOperationFromCode(codeOperation);
		BigDecimal montantFacture = getMontantFactureFromOperation(operationBancaire);
		if(montantFacture.compareTo(montantFactureSelectionne) < 0) {
			reglementPartiel(echeanciers, montantFacture, operationBancaire.getTypeOperation());
			validerOperation(operationBancaire);
		}else if(montantFacture.compareTo(montantFactureSelectionne) == 0) {
			reglementExact(echeanciers, montantFacture, operationBancaire.getTypeOperation());
			validerOperation(operationBancaire);
		}else {
			reglementAvecExcedent(echeanciers, montantFacture, operationBancaire.getTypeOperation());
			System.out.println("test3");
		}
	}

    private void reglementAvecExcedent(List<EcheancierClient> echeanciers, BigDecimal montantFacture,
			TypeOperation typeOperation) {
		// TODO Auto-generated method stub
		
	}

	private void reglementExact(List<EcheancierClient> echeanciers, BigDecimal montantFacture,
			TypeOperation typeOperation) {
		for (EcheancierClient echeance : echeanciers) {
			if(montantFacture.compareTo(BigDecimal.ZERO) > 0) {
				echeance.setMontantPaye(echeance.getMontantFacture());
				echeance.setResteAPayer(BigDecimal.ZERO);
				montantFacture = montantFacture.subtract(echeance.getMontantFacture());
				echeancierClientRepository.save(echeance);
				verserCompteSociete(echeance, typeOperation);
			}
		}

	}

	private void reglementPartiel(List<EcheancierClient> echeanciers, BigDecimal montantFacture,
    		TypeOperation typeOperation) {
    	for(EcheancierClient echeance : echeanciers) {
    		if(montantFacture.compareTo(echeance.getMontantFacture()) > 0) {
    			reglementExact(Arrays.asList(echeance), montantFacture, typeOperation);
    		}else {
    			echeance.setMontantPaye(montantFacture);
    			echeance.setResteAPayer(echeance.getMontantFacture().subtract(montantFacture));
    			montantFacture = BigDecimal.ZERO;
    			echeancierClientRepository.save(echeance);
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
    
    private void validerOperation(OperationBancaire operationBancaire) {
    	operationBancaire.setStatutOperation(StatutOperation.VALIDE);
		operationBancaireRepository.save(operationBancaire);
    }
    
    private void verserCompteSociete(EcheancierClient echeance, TypeOperation typeOperation) {
    	Banque banque = banqueRepository.getOne(echeance.getSociete().getBanque().getIdBanque());
    	//Banque banque = echeance.getSociete().getBanque().getIdBanque();
    	if(TypeOperation.CREDIT.equals(typeOperation)) {
    		BigDecimal montantExistant = banque.getSoldeCredit();
    		BigDecimal nouveauMontant = echeance.getMontantFacture().add(montantExistant);
    		banque.setSoldeCredit(nouveauMontant);
    	}else if(TypeOperation.DEBIT.equals(typeOperation)) {
    		BigDecimal montantExistant = banque.getSoldeDebit();
    		BigDecimal nouveauMontant = echeance.getMontantFacture().add(montantExistant);
    		banque.setSoldeDebit(nouveauMontant);
    	}
    	banqueRepository.save(banque);
		
	}
}
