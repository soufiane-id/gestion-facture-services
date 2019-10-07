package ma.sid;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ma.sid.dao.BanqueRepository;
import ma.sid.dao.ClientRepository;
import ma.sid.dao.EcheancierClientRepository;
import ma.sid.dao.EcheancierFournisseurRepository;
import ma.sid.dao.FournisseurRepository;
import ma.sid.dao.OperationBancaireRepository;
import ma.sid.dao.ReglementRepository;
import ma.sid.dao.SocieteRepository;
import ma.sid.entities.Banque;
import ma.sid.entities.Client;
import ma.sid.entities.EcheancierClient;
import ma.sid.entities.EcheancierFournisseur;
import ma.sid.entities.Fournisseur;
import ma.sid.entities.OperationBancaire;
import ma.sid.entities.Reglement;
import ma.sid.entities.Societe;
import ma.sid.entities.StatutOperation;

@SpringBootApplication
public class GestionFacturesServiceApplication implements CommandLineRunner{

	@Autowired
	private EcheancierClientRepository echeancierClient;
	@Autowired
	private EcheancierFournisseurRepository echeancierFournisseur;
	@Autowired
	private BanqueRepository banqueRepository;
	@Autowired
	private SocieteRepository societeRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private FournisseurRepository fournisseurRepository;
	@Autowired
	private OperationBancaireRepository operationBancaireRepository;
	@Autowired
	private ReglementRepository reglementRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(GestionFacturesServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Banque bq1 = banqueRepository.save(new Banque(null, "Banque Teralal", BigDecimal.ZERO, BigDecimal.ZERO));
		Banque bq2 = banqueRepository.save(new Banque(null, "Banque Viandes d'exception", BigDecimal.ZERO, BigDecimal.ZERO));
		Societe soc1 = societeRepository.save(new Societe(null, "Teralal", bq1));
		Societe soc2 = societeRepository.save(new Societe(null, "Viandes d'exception", bq2));
		Client client0 = clientRepository.save(new Client(null, "BOUCHERIE 4 CHEMINS"));
		Client client = clientRepository.save(new Client(null, "BOUCHERIE LAMANA"));
		Client client2 = clientRepository.save(new Client(null, "PLEIN FRAIS"));
		Client client3 = clientRepository.save(new Client(null, "AMANOUZE"));
		Client client4 = clientRepository.save(new Client(null, "BOUCHERIE AL SUNNA"));
		Client client5 = clientRepository.save(new Client(null, "I B BOUCHERIE"));
		Client client6 = clientRepository.save(new Client(null, "BISMIL"));
		Client client7 = clientRepository.save(new Client(null, "MJ VAL PRIM"));
		Client client8 = clientRepository.save(new Client(null, "LE BONHEUR CHANTELOUP"));
		Client client9 = clientRepository.save(new Client(null, "BOUCHERIE AMIRA"));
		Client client10 = clientRepository.save(new Client(null, "MAOUI"));
		Client client11= clientRepository.save(new Client(null, "M2"));
		Client client12= clientRepository.save(new Client(null, "BIE FRANCO MUSULMANE"));
		Client client21= clientRepository.save(new Client(null, "BIE FARAH"));
		Client client22= clientRepository.save(new Client(null, "LES HALLES DE PARAY"));
		Client client13= clientRepository.save(new Client(null, "BOUCHERIE MARHABA"));
		Client client14= clientRepository.save(new Client(null, "BOUCHERIE VOLTAIRE"));
		Client client15= clientRepository.save(new Client(null, "BIE DES PYRENEES"));
		Client client16= clientRepository.save(new Client(null, "HALLES DE CHANTELOUP"));
		Client client17= clientRepository.save(new Client(null, "SELF DRAVEIL"));
		Client client18= clientRepository.save(new Client(null, "LA CENTRALE DU FRAIS"));
		Client client19= clientRepository.save(new Client(null, "LA FERME DE SENIA"));
		Client client20= clientRepository.save(new Client(null, "MARCHE ENTREPOT"));
		Client client23= clientRepository.save(new Client(null, "ESPACE VERT"));
		
		Fournisseur fournisseur = fournisseurRepository.save(new Fournisseur(null, "Fournisseur 1"));
		Fournisseur fournisseur2 = fournisseurRepository.save(new Fournisseur(null, "Fournisseur 2"));
		
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement BOUCHERIE 4 CHEMINS", null, new BigDecimal(5600), StatutOperation.CREE, null));
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement BOUCHERIE LAMANA", null, new BigDecimal(2600), StatutOperation.CREE, null));
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement LES HALLES DE PARAY", null, new BigDecimal(5000), StatutOperation.CREE, null));
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement LES HALLES DE PARAY", null, new BigDecimal(100), StatutOperation.CREE, null));
		reglementRepository.save(new Reglement(null, new BigDecimal(5400), client, null, soc1));
		
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF654641", client0, "GBufi re", "Chèque", "Décoché", new BigDecimal(5000), new BigDecimal(0), new BigDecimal(5000), new Date(), soc2));
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF465147", client, "fdefuk ge", "Chèque", "Décoché", new BigDecimal(4950), new BigDecimal(0), new BigDecimal(4950), new Date(), soc1));
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF465147", client0, "fdefuk ge", "Chèque", "Décoché", new BigDecimal(900), new BigDecimal(0), new BigDecimal(900), new Date(), soc2));
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF465147", client0, "fdefuk ge", "Chèque", "Décoché", new BigDecimal(2900), new BigDecimal(0), new BigDecimal(2900), new Date(), soc1));
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF465147", client0, "fdefuk ge", "Chèque", "Décoché", new BigDecimal(1800), new BigDecimal(0), new BigDecimal(1800), new Date(), soc1));
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF465147", client22, "fdefuk ge", "Chèque", "Décoché", new BigDecimal(1800), new BigDecimal(0), new BigDecimal(1800), new Date(), soc2));
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF465147", client22, "fdefuk ge", "Chèque", "Décoché", new BigDecimal(2400), new BigDecimal(0), new BigDecimal(2400), new Date(), soc2));
		echeancierClient.save(new EcheancierClient(null, new Date(), new Date(), "AF465147", client22, "fdefuk ge", "Chèque", "Décoché", new BigDecimal(900), new BigDecimal(0), new BigDecimal(900), new Date(), soc2));
		echeancierFournisseur.save(new EcheancierFournisseur(null, new Date(), new Date(), 3800D, 900D, 2900D, new Date(), fournisseur));
		echeancierFournisseur.save(new EcheancierFournisseur(null, new Date(), new Date(), 4780D, 1400D, 2900D, new Date(), fournisseur2));
	}

}
