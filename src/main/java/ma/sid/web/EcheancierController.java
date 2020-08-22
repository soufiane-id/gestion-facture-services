package ma.sid.web;

import ma.sid.entities.*;
import ma.sid.exceptions.NonUniqueDocumentException;
import ma.sid.exceptions.PersonneNotFoundException;
import ma.sid.services.EcheancierService;
import ma.sid.services.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

//@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class EcheancierController {

	private EcheancierService echeancierService;
	private PersonneService personneService;

	@Autowired
	public EcheancierController(EcheancierService echeancierService, PersonneService personneService){
		this.echeancierService = echeancierService;
		this.personneService = personneService;
	}

	@GetMapping("/listEcheancierClient")
	public List<Echeancier> findAll() {
		return echeancierService.findAllEcheancesClient();
	}

	@GetMapping("/listEcheancierClientNonRegle")
	public List<Echeancier> findAllEcheancesNonRegles() {
		return echeancierService.findAllEcheancesClientNonRegles();
	}

	@GetMapping("/listEcheancierClientRegle")
	public List<Echeancier> findAllEcheancesRegles() {
		return echeancierService.findAllEcheancesClientRegles();
	}

	@GetMapping("/listEcheancierFournisseurNonRegle")
	public List<Echeancier> findAllEcheancesFrsNonRegles() {
		return echeancierService.findAllEcheancesFournisseurNonRegles();
	}

	@GetMapping("/listEcheancierFournisseurRegle")
	public List<Echeancier> findAllEcheancesFrsRegles() {
		return echeancierService.findAllEcheancesFournisseurRegles();
	}

	@GetMapping("/listEcheancierParOperation/{operationBancaireId}")
	public List<Echeancier> recupererEcheancesParOperation(@PathVariable Long operationBancaireId) {
		return echeancierService.recupererEcheancesParOperation(operationBancaireId);
	}

	@GetMapping("/creancesClient")
	public BigDecimal recupererCreancesClients() {
		return echeancierService.recupererCreancesClients();
	}

	@GetMapping("/dettesFournisseurs")
	public BigDecimal recupererDettesFrs() {
		return echeancierService.recupererDettesFrs();
	}

	@PostMapping("/echeancierClients")
	public ResponseEntity<Echeancier> ajouterEcheancierClient(@RequestBody Echeancier echeancier) {
		try{
			return new ResponseEntity<Echeancier>(echeancierService.ajouterEcheancier(echeancier), HttpStatus.OK);
		}catch (PersonneNotFoundException e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/integrerEcheanciers")
	public List<Echeancier> integrerEcheanciers(@RequestBody List<Echeancier> echeanciers){
		return echeancierService.integrerEcheanciers(echeanciers);
	}

	/**
	 * Recupere les echeancies non reglees d un client.
	 * @param idPersonne
	 * @return
	 */
	@GetMapping("/getEcheanciersByPersonne")
	public List<Echeancier> getEcheanciersByPersonne(Long idPersonne, Long idSociete){
		return echeancierService.getEcheanciersByPersonne(idPersonne, idSociete);
	}

	@GetMapping("/getEcheanciersByMontants/{montants}/{idPersonne}")
	public Set<Echeancier> getEcheanciersByMontants(@PathVariable List<BigDecimal> montants,
													@PathVariable Long idPersonne){
		return echeancierService.recupererEcheanciersParMontants(montants, idPersonne);
	}

	@GetMapping("/getEcheanciersByDocument/{numerosDocument}")
	public ResponseEntity<List<Echeancier>> getFactureByNumeroDocument(@PathVariable List<String> numerosDocument){
		try {
			return new ResponseEntity<>(echeancierService.recupererEcheanciersParNumerosDocument(numerosDocument),
					HttpStatus.OK);
		}catch (NonUniqueDocumentException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
