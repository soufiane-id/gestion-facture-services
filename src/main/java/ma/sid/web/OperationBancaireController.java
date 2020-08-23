package ma.sid.web;

import ma.sid.entities.OperationBancaire;
import ma.sid.services.OperationBancaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class OperationBancaireController {

	private OperationBancaireService operationBancaireService;

	@Autowired
	public OperationBancaireController(OperationBancaireService operationBancaireService){
		this.operationBancaireService = operationBancaireService;
	}
	
	@GetMapping("/listOperationBancaire")
    List<OperationBancaire> findAll() {
        return operationBancaireService.recupererOperationsBancaires();
    }

	@GetMapping("/operationsBancairesValides")
	List<OperationBancaire> findAllOperationsValides() {
		return operationBancaireService.recupererOperationsBancairesValides();
	}

	@GetMapping("/operationsBancairesATraiter")
	List<OperationBancaire> findAllOperationsATraiter() {
		return operationBancaireService.recupererOperationsBancairesATraiter();
	}

	@GetMapping("/operationsTransfertBancaire")
	List<OperationBancaire> findAllTransfertsDArgent() {
		return operationBancaireService.recupererTransfertsArgent();
	}

	@GetMapping("/operationsAttenteFacture")
	List<OperationBancaire> findAllOperationsEnAttenteFacture() {
		return operationBancaireService.recupererOperationEnAttenteFacture();
	}

	@GetMapping("/operationsChargeFixe")
	List<OperationBancaire> findAllOperationsChargesFixes() {
		return operationBancaireService.recupererOperationChargesFixes();
	}

	@GetMapping("/operationsImpaye")
	List<OperationBancaire> findAllOperationsImpayes() {
		return operationBancaireService.recupererOperationImpayes();
	}

	@PostMapping("/operationsBancaires")
	OperationBancaire ajouterOperationsBancaires(@RequestBody OperationBancaire operation) throws Exception {
		return operationBancaireService.ajouterOperationBancaire(operation);
	}
	
	@PostMapping("/integrerOperations")
	List<OperationBancaire> integrerOperations(@RequestBody List<OperationBancaire> operations) throws Exception {
		return operationBancaireService.integererOperations(operations);
	}

	@DeleteMapping("/deleteOperation/{codeOperation}")
	public ResponseEntity<Void> deleteOperation(@PathVariable Long codeOperation) {
		operationBancaireService.supprimerOperation(codeOperation);
		return ResponseEntity.ok().build();
	}

}
