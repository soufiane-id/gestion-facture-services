//package ma.sid.services;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import ma.sid.dao.BanqueRepository;
//import ma.sid.dao.ClientRepository;
//import ma.sid.dao.OperationBancaireRepository;
//import ma.sid.dao.SocieteRepository;
//import ma.sid.entities.Banque;
//import ma.sid.entities.Client;
//import ma.sid.entities.EcheancierClient;
//import ma.sid.entities.OperationBancaire;
//import ma.sid.entities.Societe;
//import ma.sid.dto.enums.StatutOperation;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class ReglementServiceTest {
// 
//	  @Mock
//	  private ReglementClientService reglementService;
//	  @Mock
//	  private ClientRepository clientRepository;
//	  @Mock
//	  private SocieteRepository societeRepository;
//	  @Mock
//	  private BanqueRepository banqueRepository;
//	  
//	  private Client client;
//	  private Banque teralal;
//	  private Societe societe;
//	  private OperationBancaire operationBancaire;
//
//	  @BeforeEach
//	  void initUseCase() {
//		  teralal = new Banque(null, "Banque Teralal", BigDecimal.ZERO, BigDecimal.ZERO);
//		  client = new Client(null, "BOUCHERIE 4 CHEMINS");
//		  societe = new Societe(null, "Teralal", teralal);
//		  operationBancaire = new OperationBancaire(null, new Date(), "Virement BOUCHERIE 4 CHEMINS", null, new BigDecimal(5600), StatutOperation.A_TRAITER, null);
//		  when(clientRepository.save(any(Client.class))).thenReturn(client);
//		  when(banqueRepository.save(any(Banque.class))).thenReturn(teralal);
//		  when(societeRepository.save(any(Societe.class))).thenReturn(societe);
//		  when(reglementService.getOperationFromCode(1L)).thenReturn(operationBancaire);
//	  }
//	  
//	  @Test
//	  void testReglementExact() {
//		  List<EcheancierClient> echeanciers = new ArrayList<>();
//		  echeanciers.add(new EcheancierClient(null, null, null, null, client, null, null, null, new BigDecimal(5000), BigDecimal.ZERO, new BigDecimal(5000), null, societe));
//	      reglementService.reglerOperationsBancaires(echeanciers, 1L, new BigDecimal(5000));
//	  }
//
//}