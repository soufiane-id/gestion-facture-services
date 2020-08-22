package ma.sid.services;

import ma.sid.dao.*;
import ma.sid.dto.enums.StatutOperation;
import ma.sid.dto.enums.TypePersonne;
import ma.sid.entities.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReglementVirementServiceImplIT {

    @TestConfiguration
    static class ReglementServiceImplTestContextConfiguration {

        @Bean
        public ReglementService employeeService() {
            return new ReglementServiceImpl();
        }
    }

    @Autowired
    private ReglementService reglementService;

    @MockBean
    private EcheancierRepository echeancierRepository;
    @MockBean
    private OperationBancaireRepository operationBancaireRepository;
    @MockBean
    private BanqueRepository banqueRepository;
    @MockBean
    private PersonneRepository personneRepository;
    @MockBean
    private SocieteRepository societeRepository;

    private Banque banque;
    private Personne client;
    private Societe societe;
    private OperationBancaire operationBancaire;
    private OperationBancaire operationDebit;
    private List<Echeancier> excedent = new ArrayList<>();
    private Banque resultatBanque;

    @Before
    public void setUp() {
        banque = new Banque(1L, "Banque 1", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        client = new Personne(1L, "client 1", TypePersonne.CLIENT);
        societe = new Societe(1L, "societe 1", banque);

        operationBancaire = new OperationBancaire(1L, new Date(), "Opération Test", null, new BigDecimal("3612.49"), StatutOperation.A_TRAITER, null, null);
        operationDebit = new OperationBancaire(2L, new Date(), "Opération Impayé", new BigDecimal("-2568.14"), null, StatutOperation.A_TRAITER, null, null);
        when(operationBancaireRepository.getOne(1L)).thenReturn(operationBancaire);
        when(operationBancaireRepository.getOne(2L)).thenReturn(operationDebit);
        when(banqueRepository.getOne(1L)).thenReturn(banque);
        when(banqueRepository.save(Mockito.any(Banque.class))).thenReturn(banque);

        // On recupere la facture à enregistrer
        when(echeancierRepository.save(Mockito.any(Echeancier.class))).thenAnswer((Answer<Echeancier>) invocation -> {
            if(((Echeancier) invocation.getArguments()[0]).getNumeroDocument().contains("EXCEDENT")){
                excedent.add((Echeancier) invocation.getArguments()[0]);
            } else if(((Echeancier) invocation.getArguments()[0]).getNumeroDocument().contains("IMPAYE")) {
                excedent.add((Echeancier) invocation.getArguments()[0]);
            }
            return (Echeancier) invocation.getArguments()[0];
        });

        /*when(banqueRepository.save(Mockito.any(Banque.class)))
                .thenAnswer(i ->
                        {
                            //resultatBanque = (Banque) i.getArguments()[0];
                            return i.getArguments()[0];
                        });*/
    }

    /**
     * Scénario: Réglement partiel dune facture entamée ayant un rap de 5241.36 par une opération ayant un montant de 3612.49
     */
    @Test
    public void whenReglementPartiel_UneFacture() {
        List<Echeancier> echeanciers = new ArrayList<>();
        BigDecimal montantPaye = new BigDecimal("3712.95");
        BigDecimal rap1 = new BigDecimal("5241.36");

        Echeancier facture1 = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal("8954.32"), montantPaye, rap1, new Date(), societe);
        echeanciers.add(facture1);

        reglementService.reglerOperationBancaireParVirement(echeanciers, 1L, rap1);

        assertThat(echeanciers.get(0).getMontantFacture()).isEqualTo(new BigDecimal("8954.32"));
        assertThat(echeanciers.get(0).getResteAPayer()).isEqualTo(rap1.subtract(operationBancaire.getMontantCredit()));
        assertThat(echeanciers.get(0).getMontantPaye()).isEqualTo(montantPaye.add(operationBancaire.getMontantCredit()));
        assertThat(echeanciers.get(0).getOperations().size()).isEqualTo(1);

        Mockito.verify(echeancierRepository, VerificationModeFactory.times(1)).save(Mockito.any(Echeancier.class));
        Mockito.verify(banqueRepository, VerificationModeFactory.times(1)).save(Mockito.any(Banque.class));

        //On test le montant qui sera versé à la banque
        assertThat(banque.getSoldeCredit()).isEqualTo(operationBancaire.getMontantCredit());

    }

    /**
     * Scénario : Réglement en excédent dune facture ayant un montant de 1000 par une opération ayant un montant de 3612.49
     */
    @Test
    public void whenReglementExcedent_UneFacture() {

        List<Echeancier> echeanciers = new ArrayList<>();
        BigDecimal rap1 = new BigDecimal("1000");
        Echeancier facture1000 = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal("1000"), new BigDecimal("0"), rap1, new Date(), societe);
        echeanciers.add(facture1000);

        reglementService.reglerOperationBancaireParVirement(echeanciers, 1L, rap1);

        assertThat(echeanciers.get(0).getResteAPayer()).isEqualTo(BigDecimal.ZERO);
        assertThat(echeanciers.get(0).getMontantFacture()).isEqualTo(new BigDecimal("1000"));
        assertThat(echeanciers.get(0).getMontantPaye()).isEqualTo(echeanciers.get(0).getMontantFacture());
        assertThat(echeanciers.get(0).getOperations().size()).isEqualTo(1);

        // Il reste a tester les montants de la facture excedent.
        assertThat(excedent.get(0).getMontantFacture()).isEqualTo(rap1.subtract(operationBancaire.getMontantCredit()));
        assertThat(excedent.get(0).getMontantPaye()).isEqualTo(BigDecimal.ZERO);
        assertThat(excedent.get(0).getResteAPayer()).isEqualTo(rap1.subtract(operationBancaire.getMontantCredit()));
        assertThat(excedent.get(0).getOperations()).isNull();

        //On appelle 2 fois la methode save ( une fois pour sauvegarder la facture et la 2eme fois pour enregistrer lexcedent)
        Mockito.verify(echeancierRepository, VerificationModeFactory.times(2)).save(Mockito.any(Echeancier.class));
        Mockito.verify(banqueRepository, VerificationModeFactory.times(2)).save(Mockito.any(Banque.class));

        //On test le montant qui sera versé à la banque
        assertThat(banque.getSoldeCredit()).isEqualTo(operationBancaire.getMontantCredit());
    }

    /**
     * Scénario : Réglement en excédent de deux factures ayant un montant de 1800 par une opération ayant un montant de 3612.49
     */
    @Test
    public void whenReglementExcedent_DeuxFactures() {
        List<Echeancier> echeanciers = new ArrayList<>();
        BigDecimal rap1 = new BigDecimal("1000");
        BigDecimal rap2 = new BigDecimal("800");

        BigDecimal sommeRAPs = rap1.add(rap2);

        Echeancier facture1000 = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal(1000), new BigDecimal(0), rap1, new Date(), societe);
        Echeancier facture800 = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal(800), new BigDecimal(0), rap2, new Date(), societe);
        echeanciers.add(facture1000);
        echeanciers.add(facture800);

        reglementService.reglerOperationBancaireParVirement(echeanciers, 1L, sommeRAPs);

        assertThat(echeanciers.get(0).getResteAPayer()).isEqualTo(BigDecimal.ZERO);
        assertThat(echeanciers.get(0).getMontantFacture()).isEqualTo(new BigDecimal("1000"));
        assertThat(echeanciers.get(0).getMontantPaye()).isEqualTo(echeanciers.get(0).getMontantFacture());
        assertThat(echeanciers.get(0).getOperations().size()).isEqualTo(1);

        assertThat(echeanciers.get(1).getResteAPayer()).isEqualTo(BigDecimal.ZERO);
        assertThat(echeanciers.get(1).getMontantFacture()).isEqualTo(new BigDecimal(800));
        assertThat(echeanciers.get(1).getMontantPaye()).isEqualTo(echeanciers.get(1).getMontantFacture());
        assertThat(echeanciers.get(1).getOperations().size()).isEqualTo(1);

        Mockito.verify(echeancierRepository, VerificationModeFactory.times(3)).save(Mockito.any(Echeancier.class));

        // Il reste a tester les montants de la facture excedent.
        assertThat(excedent.get(0).getMontantFacture()).isEqualTo(sommeRAPs.subtract(operationBancaire.getMontantCredit()));
        assertThat(excedent.get(0).getMontantPaye()).isEqualTo(BigDecimal.ZERO);
        assertThat(excedent.get(0).getResteAPayer()).isEqualTo(sommeRAPs.subtract(operationBancaire.getMontantCredit()));
        assertThat(excedent.get(0).getOperations()).isNull();

        //On test le montant qui sera versé à la banque
        assertThat(banque.getSoldeCredit()).isEqualTo(operationBancaire.getMontantCredit());
    }

    /**
     * Scénario : Réglement en excédent de trois factures (deja entamées )
     * par une opération
     */
    @Test
    public void whenReglementExcedent_TroisFacturesEntamees() {
        List<Echeancier> echeanciers = new ArrayList<>();
        BigDecimal rap1 = new BigDecimal("1541.25");
        BigDecimal rap2 = new BigDecimal("654.17");
        BigDecimal rap3 = new BigDecimal("493.73");

        BigDecimal sommeRAPs = rap1.add(rap2.add(rap3));

        Echeancier facture1 = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal("2568.14"), new BigDecimal("1026.89"), rap1, new Date(), societe);
        Echeancier facture2 = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal("1695.69"), new BigDecimal("1041.52"), rap2, new Date(), societe);
        Echeancier facture3 = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal("2457.91"), new BigDecimal("1964.18"), rap3, new Date(), societe);
        echeanciers.add(facture1);
        echeanciers.add(facture2);
        echeanciers.add(facture3);

        reglementService.reglerOperationBancaireParVirement(echeanciers, 1L, sommeRAPs);
        //Facture 1
        assertThat(echeanciers.get(0).getResteAPayer()).isEqualTo(BigDecimal.ZERO);
        assertThat(echeanciers.get(0).getMontantFacture()).isEqualTo(new BigDecimal("2568.14"));
        assertThat(echeanciers.get(0).getMontantPaye()).isEqualTo(new BigDecimal("2568.14"));
        assertThat(echeanciers.get(0).getOperations().size()).isEqualTo(1);
        //Facture 2
        assertThat(echeanciers.get(1).getResteAPayer()).isEqualTo(BigDecimal.ZERO);
        assertThat(echeanciers.get(1).getMontantFacture()).isEqualTo(new BigDecimal("1695.69"));
        assertThat(echeanciers.get(1).getMontantPaye()).isEqualTo(new BigDecimal("1695.69"));
        assertThat(echeanciers.get(1).getOperations().size()).isEqualTo(1);
        //Facture 3
        assertThat(echeanciers.get(2).getResteAPayer()).isEqualTo(BigDecimal.ZERO);
        assertThat(echeanciers.get(2).getMontantFacture()).isEqualTo(new BigDecimal("2457.91"));
        assertThat(echeanciers.get(2).getMontantPaye()).isEqualTo(new BigDecimal("2457.91"));
        assertThat(echeanciers.get(2).getOperations().size()).isEqualTo(1);

        Mockito.verify(echeancierRepository, VerificationModeFactory.times(4)).save(Mockito.any(Echeancier.class));

        // Il reste a tester les montants de la facture excedent.
        assertThat(excedent.get(0).getMontantFacture()).isEqualTo(sommeRAPs.subtract(operationBancaire.getMontantCredit()));
        assertThat(excedent.get(0).getMontantPaye()).isEqualTo(BigDecimal.ZERO);
        assertThat(excedent.get(0).getResteAPayer()).isEqualTo(sommeRAPs.subtract(operationBancaire.getMontantCredit()));
        assertThat(excedent.get(0).getOperations()).isNull();

        //On test le montant qui sera versé à la banque
        assertThat(banque.getSoldeCredit()).isEqualTo(operationBancaire.getMontantCredit());
    }

    /**
     * Scénario:
     */
    @Test
    public void whenReglementImpaye() {
        Echeancier facture = new Echeancier(1L, new Date(), new Date(), "AF654641", client, "GBufi re", null, "Décoché", new BigDecimal("2568.14"), BigDecimal.ZERO, new BigDecimal("2568.14"), new Date(), societe);

        reglementService.reglerImpaye(Arrays.asList(facture), 2L);

        assertThat(excedent.get(0).getResteAPayer()).isEqualTo(operationDebit.getMontantDebit().negate());
        assertThat(excedent.get(0).getMontantFacture()).isEqualTo(operationDebit.getMontantDebit().negate());
        assertThat(excedent.get(0).getMontantPaye()).isEqualTo(BigDecimal.ZERO);
        assertThat(excedent.get(0).getOperations()).isNullOrEmpty();

        Mockito.verify(echeancierRepository, VerificationModeFactory.times(1)).save(Mockito.any(Echeancier.class));
        assertThat(operationDebit.getStatutOperation()).isEqualTo(StatutOperation.IMPAYE);

        //On test le montant qui sera versé à la banque
        assertThat(banque.getSoldeDebit()).isEqualTo(operationDebit.getMontantDebit().negate());

    }
}
