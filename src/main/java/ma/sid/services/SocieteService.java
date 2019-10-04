package ma.sid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.SocieteRepository;
import ma.sid.entities.Societe;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class SocieteService {

	@Autowired
	private SocieteRepository societeRepository;
	
	@GetMapping("/listSociete")
    List<Societe> findAll() {
        return societeRepository.findAll();
    }
}
