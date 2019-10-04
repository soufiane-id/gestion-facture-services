package ma.sid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.ReglementRepository;
import ma.sid.entities.OperationBancaire;
import ma.sid.entities.Reglement;
import ma.sid.entities.StatutOperation;
import ma.sid.entities.TypeReglement;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class ReglementService {

	@Autowired
	private ReglementRepository reglementRepository;
	
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
}
