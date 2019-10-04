package ma.sid.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.ClientRepository;
import ma.sid.entities.Client;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@GetMapping("/listClient")
    List<Client> findAll() {
        return clientRepository.findAll();
    }
	
	@PostMapping("/clients")
    Client ajotuerClient(@RequestBody Client client) throws Exception {
		try {
			return clientRepository.save(client);
		}catch (Exception e) {
			throw new Exception("Erreur lors de l'ajout. Vérifer que ce client n'existe pas déjà");
		}
    }
	
	@DeleteMapping("/deleteClient/{idClient}")
    public void deleteClient(@PathVariable Long idClient) {
        clientRepository.deleteById(idClient);
    }
}
