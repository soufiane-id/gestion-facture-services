package ma.sid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.OperationBancaireRepository;
import ma.sid.entities.OperationBancaire;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class OperationBancaireService {

	@Autowired
	private OperationBancaireRepository operationBancaireRepository;
	
	@GetMapping("/listOperationBancaire")
    List<OperationBancaire> findAll() {
        return operationBancaireRepository.findAll();
    }
}
