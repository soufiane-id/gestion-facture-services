package ma.sid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.EcheancierFournisseurRepository;
import ma.sid.entities.EcheancierFournisseur;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class EcheancierFournisseurService {

	@Autowired
	private EcheancierFournisseurRepository echeancierFournisseurRepository;
	
	@GetMapping("/listEcheancierFournisseur")
    List<EcheancierFournisseur> findAll() {
        return echeancierFournisseurRepository.findAll();
    }
}
