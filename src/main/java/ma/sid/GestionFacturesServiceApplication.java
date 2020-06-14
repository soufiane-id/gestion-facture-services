package ma.sid;

import ma.sid.dao.*;
import ma.sid.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class GestionFacturesServiceApplication implements CommandLineRunner{

	@Autowired
	private EcheancierRepository echeancierRepository;
	@Autowired
	private BanqueRepository banqueRepository;
	@Autowired
	private SocieteRepository societeRepository;
	@Autowired
	private PersonneRepository personneRepository;
	@Autowired
	private OperationBancaireRepository operationBancaireRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(GestionFacturesServiceApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
			}
		};
	}

	@Override
	public void run(String... args) throws Exception {
		/*Banque bq1 = banqueRepository.save(new Banque(null, "Banque Teralal", BigDecimal.ZERO, BigDecimal.ZERO));
		Banque bq2 = banqueRepository.save(new Banque(null, "Banque Viandes d'exception", BigDecimal.ZERO, BigDecimal.ZERO));
		Societe soc1 = societeRepository.save(new Societe(null, "Teralal", bq1));
		Societe soc2 = societeRepository.save(new Societe(null, "Viandes d'exception", bq2));
		Personne client0 = personneRepository.save(new Personne(null, "BOUCHERIE 4 CHEMINS", TypePersonne.CLIENT));
		Personne client = personneRepository.save(new Personne(null, "BOUCHERIE LAMANA", TypePersonne.CLIENT));
		Personne client2 = personneRepository.save(new Personne(null, "PLEIN FRAIS", TypePersonne.CLIENT));
		Personne client3 = personneRepository.save(new Personne(null, "AMANOUZE", TypePersonne.CLIENT));
		Personne client4 = personneRepository.save(new Personne(null, "BOUCHERIE AL SUNNA", TypePersonne.CLIENT));
		Personne client5 = personneRepository.save(new Personne(null, "I B BOUCHERIE", TypePersonne.CLIENT));
		Personne client6 = personneRepository.save(new Personne(null, "BISMIL", TypePersonne.CLIENT));
		Personne client7 = personneRepository.save(new Personne(null, "MJ VAL PRIM", TypePersonne.CLIENT));
		Personne client8 = personneRepository.save(new Personne(null, "LE BONHEUR CHANTELOUP", TypePersonne.CLIENT));
		Personne client9 = personneRepository.save(new Personne(null, "BOUCHERIE AMIRA", TypePersonne.CLIENT));
		Personne client10 = personneRepository.save(new Personne(null, "MAOUI", TypePersonne.CLIENT));
		Personne client11= personneRepository.save(new Personne(null, "M2", TypePersonne.CLIENT));
		Personne client12= personneRepository.save(new Personne(null, "BIE FRANCO MUSULMANE", TypePersonne.CLIENT));
		Personne client21= personneRepository.save(new Personne(null, "BIE FARAH", TypePersonne.CLIENT));
		Personne client22= personneRepository.save(new Personne(null, "LES HALLES DE PARAY", TypePersonne.CLIENT));
		Personne client13= personneRepository.save(new Personne(null, "BOUCHERIE MARHABA", TypePersonne.CLIENT));
		Personne client14= personneRepository.save(new Personne(null, "BOUCHERIE VOLTAIRE", TypePersonne.CLIENT));
		Personne client15= personneRepository.save(new Personne(null, "BIE DES PYRENEES", TypePersonne.CLIENT));
		Personne client16= personneRepository.save(new Personne(null, "HALLES DE CHANTELOUP", TypePersonne.CLIENT));
		Personne client17= personneRepository.save(new Personne(null, "SELF DRAVEIL", TypePersonne.CLIENT));
		Personne client18= personneRepository.save(new Personne(null, "LA CENTRALE DU FRAIS", TypePersonne.CLIENT));
		Personne client19= personneRepository.save(new Personne(null, "LA FERME DE SENIA", TypePersonne.CLIENT));
		Personne client20= personneRepository.save(new Personne(null, "MARCHE ENTREPOT", TypePersonne.CLIENT));
		Personne client23= personneRepository.save(new Personne(null, "ESPACE VERT", TypePersonne.CLIENT));
		Personne client233= personneRepository.save(new Personne(null, "BOUCHERIE 2002", TypePersonne.CLIENT));
		Personne client24= personneRepository.save(new Personne(null, "V.EXPT", TypePersonne.CLIENT));
		Personne client25= personneRepository.save(new Personne(null, "PARTICULIER", TypePersonne.CLIENT));
		Personne client26= personneRepository.save(new Personne(null, "BOUCHERIE DES 10 ARPENTS", TypePersonne.CLIENT));
		Personne client27= personneRepository.save(new Personne(null, "BOUCHERIE BMB", TypePersonne.CLIENT));
		Personne client28= personneRepository.save(new Personne(null, "ETS PUIGRENIER", TypePersonne.CLIENT));
		Personne client29= personneRepository.save(new Personne(null, "BOUCHERIE GENTILLEENNE", TypePersonne.CLIENT));
		Personne client30= personneRepository.save(new Personne(null, "BOUCHERIE D'EPINAY", TypePersonne.CLIENT));
		Personne client31= personneRepository.save(new Personne(null, "BOUCHERIE HAJJAR", TypePersonne.CLIENT));
		Personne client32= personneRepository.save(new Personne(null, "DGL DISTRIBUTION", TypePersonne.CLIENT));
		Personne client33= personneRepository.save(new Personne(null, "BIE REHAB 2", TypePersonne.CLIENT));
		Personne client34= personneRepository.save(new Personne(null, "BIE REHAB 1", TypePersonne.CLIENT));
		Personne client35= personneRepository.save(new Personne(null, "JARDIN DU VAL", TypePersonne.CLIENT));
		Personne client36= personneRepository.save(new Personne(null, "BOUCHERIE KARROUM", TypePersonne.CLIENT));
		Personne client37= personneRepository.save(new Personne(null, "LA GRANDE BOUCHERIE", TypePersonne.CLIENT));
		Personne client38= personneRepository.save(new Personne(null, "BELLE VILLE ALIMENTATION BOUDALIA", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BAYAMED", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE REHAB", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "FERME DE SENIA", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE 4 ETOILES", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE L'ESPOIR", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "TADFOUSSE", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "ART VIANDES", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "WISSAM 2", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE SAS W&R", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "ROUIBA", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE FERME DE CARRIERES", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE HOGGAR", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE ISTANBUL", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE AMNAGOR", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE 4 SAISONS NEUILLY", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE ASSALAM VILLEJUIF", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "IRISH COUNTRY MEAT", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "AFOULKI", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE EL SALEM", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE KARAM NANTERRE", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE SELMA", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE EURO PRIMEUR", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE TROIS COMMUNES", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE KABIRA 1", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE LIMEIL BREVANNES", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOUCHERIE YATRIB", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BOCHERIE KABIRA 2", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE LAMHAYA", TypePersonne.CLIENT));
		personneRepository.save(new Personne(null, "BIE KENZI", TypePersonne.CLIENT));
		
		Personne fournisseur = personneRepository.save(new Personne(null, "BRETAGNE VIANDES", TypePersonne.FOURNISSEUR));
		Personne fournisseur2 = personneRepository.save(new Personne(null, "TRADIVAL", TypePersonne.FOURNISSEUR));
		Personne fournisseur3 = personneRepository.save(new Personne(null, "TENDRIADE", TypePersonne.FOURNISSEUR));


		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement BOUCHERIE 4 CHEMINS", null, new BigDecimal(5600), StatutOperation.A_TRAITER, null, null));
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement BOUCHERIE LAMANA", null, new BigDecimal(2600), StatutOperation.A_TRAITER, null, null));
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement LES HALLES DE PARAY", null, new BigDecimal(5000), StatutOperation.A_TRAITER, null, null));
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Virement LES HALLES DE PARAY", null, new BigDecimal(100), StatutOperation.A_TRAITER, null, null));
		operationBancaireRepository.save(new OperationBancaire(null, new Date(), "Paiement Bretagne Viandes", new BigDecimal(-6800), null, StatutOperation.A_TRAITER, null, null));

		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF654641", client0, "GBufi re", null, "Décoché", new BigDecimal(5000), new BigDecimal(0), new BigDecimal(5000), new Date(), soc2));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF465147", client, "fdefuk ge", null, "Décoché", new BigDecimal(4950), new BigDecimal(0), new BigDecimal(4950), new Date(), soc1));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF465147", client0, "fdefuk ge", null, "Décoché", new BigDecimal(900), new BigDecimal(0), new BigDecimal(900), new Date(), soc2));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF465147", client0, "fdefuk ge", null, "Décoché", new BigDecimal(2900), new BigDecimal(0), new BigDecimal(2900), new Date(), soc1));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF465147", client0, "fdefuk ge", null, "Décoché", new BigDecimal(1800), new BigDecimal(0), new BigDecimal(1800), new Date(), soc1));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF465147", client22, "fdefuk ge", null, "Décoché", new BigDecimal(1700), new BigDecimal(0), new BigDecimal(1700), new Date(), soc2));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF465147", client22, "fdefuk ge", null, "Décoché", new BigDecimal(2400), new BigDecimal(0), new BigDecimal(2400), new Date(), soc2));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF465147", client22, "fdefuk ge", null, "Décoché", new BigDecimal(900), new BigDecimal(0), new BigDecimal(900), new Date(), soc2));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF4648952", fournisseur, "GBufi re", null, "Décoché", new BigDecimal(6800), new BigDecimal(0), new BigDecimal(6800), new Date(), soc1));
		echeancierRepository.save(new Echeancier(null, new Date(), new Date(), "AF4648953", fournisseur, "GBufi rfrhe", null, "Décoché", new BigDecimal(1400), new BigDecimal(0), new BigDecimal(1400), new Date(), soc2));*/
	}

}
