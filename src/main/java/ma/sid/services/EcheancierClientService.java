package ma.sid.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.sid.dao.ClientRepository;
import ma.sid.dao.EcheancierClientRepository;
import ma.sid.entities.Client;
import ma.sid.entities.EcheancierClient;

@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class EcheancierClientService {

	@Autowired
	private EcheancierClientRepository echeancierClientRepository;
	@Autowired
	private ClientRepository clientRepository;
	
	@GetMapping("/listEcheancierClient")
    List<EcheancierClient> findAll() {
        return echeancierClientRepository.findAll();
    }
	
	@GetMapping("/listEcheancierClientNonRegle")
    List<EcheancierClient> findAllNonRegles() {
		return echeancierClientRepository.findAllEcheancesNonRegles();
    }
	
	@GetMapping("/listEcheancierClientRegle")
    List<EcheancierClient> findAllRegles() {
		return echeancierClientRepository.findAllEcheancesRegles();
    }
	
    @PostMapping("/echeancierClients")
    EcheancierClient ajotuerEcheancierClient(@RequestBody EcheancierClient echeancierClient) throws Exception {
    	// Lors de l'ajout, on force le RAP au montant de la facture, en attendant le réglement de l'echeance.
    	echeancierClient.setResteAPayer(echeancierClient.getMontantFacture());
    	
    	// On valorise le client 
    	if(echeancierClient.getClient() == null) {
    		Client client = findClientByName(echeancierClient.getNomClient());
    		if(client == null) {
    			throw new Exception("Client " +echeancierClient.getNomClient() + "inconnu");
    		}
    		echeancierClient.setClient(client);
    	}
        
    	return echeancierClientRepository.save(echeancierClient);
    }
    
    /**
     * Recupere les echeancies non reglees d un client.
     * @param idClient
     * @return
     */
    @GetMapping("/getEcheanciersByClient/{idClient}")
    List<EcheancierClient> getEcheanciersByClient(@PathVariable Long idClient){
    	return echeancierClientRepository.getEcheanciersByClient(idClient);
    }
    
    @GetMapping("/getEcheanciersByMontants/{montants}/{idClient}")
    private List<EcheancierClient> getEcheanciersByMontants(@PathVariable List<BigDecimal> montants, 
    		@PathVariable Long idClient){
    	List<EcheancierClient> resultat = new ArrayList<>();
    	for(BigDecimal montant: montants) {
    		resultat.add(findEcheancierByMontant(montant, idClient));
    	}
    	return resultat;
    }
    
    private Client findClientByName(String nameClient) {
    	Client client = new Client();
		client.setNomClient(nameClient);
		Example<Client> clientExample = Example.of(client);
		Iterable<Client> clients = clientRepository.findAll(clientExample);
		for (Client e : clients) {
			return e;
		}
		return null;
    }
    
    private EcheancierClient findEcheancierByMontant(BigDecimal montant, Long idClient) {
    	Client client = new Client();
		client.setIdClient(idClient);
    	EcheancierClient criteria = new EcheancierClient();
    	criteria.setMontantFacture(montant);
    	criteria.setClient(client);
		Example<EcheancierClient> echeancierExample = Example.of(criteria);
		Iterable<EcheancierClient> echeanciers = echeancierClientRepository.findAll(echeancierExample);
		for (EcheancierClient e : echeanciers) {
			return e;
		}
		return null;
    }
    
    
}
