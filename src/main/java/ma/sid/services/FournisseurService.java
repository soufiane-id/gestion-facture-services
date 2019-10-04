package ma.sid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.FournisseurRepository;
import ma.sid.entities.Fournisseur;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class FournisseurService {

	@Autowired
	private FournisseurRepository fournisseurRepository;
	
	@GetMapping("/listFournisseur")
    List<Fournisseur> findAll() {
        return fournisseurRepository.findAll();
    }
}
