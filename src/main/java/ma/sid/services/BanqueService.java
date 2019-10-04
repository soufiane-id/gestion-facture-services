package ma.sid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.BanqueRepository;
import ma.sid.entities.Banque;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class BanqueService {

	@Autowired
	private BanqueRepository banqueRepository;
	
	@GetMapping("/listBanque")
    List<Banque> findAll() {
        return banqueRepository.findAll();
    }
}
