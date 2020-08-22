package ma.sid.services;

import ma.sid.entities.Echeancier;
import ma.sid.entities.Personne;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface EcheancierService {
    List<Echeancier> findAllEcheancesClient();

    List<Echeancier> findAllEcheancesClientNonRegles();

    List<Echeancier> findAllEcheancesClientRegles();

    List<Echeancier> findAllEcheancesFournisseurNonRegles();

    List<Echeancier> findAllEcheancesFournisseurRegles();

    List<Echeancier> findAllEcheancesRegles();

    Echeancier ajouterEcheancier(Echeancier echeancier);

    List<Echeancier> integrerEcheanciers(List<Echeancier> echeanciers);

    List<Echeancier> getEcheanciersByPersonne(Long idPersonne, Long idSociete);

    Set<Echeancier> recupererEcheanciersParMontants(List<BigDecimal> montants, Long idPersonne);

    List<Echeancier> recupererEcheancesParOperation(Long operationBancaireId);

    BigDecimal recupererCreancesClients();

    BigDecimal recupererDettesFrs();

    List<Echeancier> recupererEcheanciersParNumerosDocument(List<String> numeroDocument);

    BigDecimal recupererAchatsClientParDate(Personne client, Date startWeek, Date endWeek);
}
