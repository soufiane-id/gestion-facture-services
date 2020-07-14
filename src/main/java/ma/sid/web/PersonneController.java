package ma.sid.web;

import ma.sid.entities.Personne;
import ma.sid.services.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class PersonneController {

	private PersonneService personneService;

	@Autowired
	public PersonneController(PersonneService personneService){
		this.personneService = personneService;
	}

	@GetMapping("/listClient")
	public List<Personne> findAllClients() { return personneService.recupererClients(); }

	@PostMapping("/clients")
	public ResponseEntity<Personne> ajouterClient(@RequestBody Personne personne) {
		return new ResponseEntity<Personne>(personneService.ajouterClient(personne), HttpStatus.OK);
	}

	@GetMapping("/listFournisseur")
	public List<Personne> findAllFournisseurs() {
		return personneService.recupererFournisseurs();
	}

	@PostMapping("/fournisseurs")
	public Personne ajouterFournisseur(@RequestBody Personne personne)  {
		try {
			return personneService.ajouterFournisseur(personne);
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Erreur lors de l'ajout. Vérifer que ce fournisseur n'existe pas déjà");
		}
	}

	@DeleteMapping("/deletePersonne/{idPersonne}")
	public void deletePersonne(@PathVariable Long idPersonne) {
		personneService.supprimerPersonne(idPersonne);
	}

}