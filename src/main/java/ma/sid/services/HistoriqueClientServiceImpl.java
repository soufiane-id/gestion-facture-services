package ma.sid.services;

import ma.sid.dao.HistoriqueClientRepository;
import ma.sid.dao.PersonneRepository;
import ma.sid.dto.enums.NoteClient;
import ma.sid.entities.Echeancier;
import ma.sid.entities.HistoriqueClient;
import ma.sid.entities.HistoriqueClientId;
import ma.sid.entities.Personne;
import ma.sid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoriqueClientServiceImpl implements HistoriqueClientService {

    @Autowired
    private EcheancierService echeancierService;
    @Autowired
    private HistoriqueClientRepository historiqueClientRepository;
    @Autowired
    private PaiementService paiementService;
    @Autowired
    private PersonneService personneService;

    private static final String MARGE_TOLERABLE = "0.5";

    @Override
    public List<HistoriqueClient> getHistoriqueClient(Date date, boolean refreshMode) {
        List<HistoriqueClient> listHistorique = historiqueClientRepository.findBySemaineId("S_" + DateUtils.getWeekNumberOfYear(date) + "_" + DateUtils.toLocalDate(date).getYear());
        return listHistorique;
    }

    @Override
    public void synchronize(Date date) {
        LocalDate debutSemaine = DateUtils.getStartEndWeekOfGivenDate(date).get(DateUtils.LABEL_START_WEEK);
        LocalDate finSemaine = DateUtils.getStartEndWeekOfGivenDate(date).get(DateUtils.LABEL_END_WEEK);
        LocalDate finSemaine_1 = debutSemaine.minusDays(1);
        LocalDate debutSemaine_1 = finSemaine_1.minusDays(6);
        LocalDate finSemaine_2 = debutSemaine_1.minusDays(1);
        LocalDate debutSemaine_2 = finSemaine_2.minusDays(6);

        //List<Echeancier> echeancesNonReglees = echeancierService.findAllEcheancesClientNonRegles();
        //Set<Personne> clients = echeancesNonReglees.stream().map(e -> e.getPersonne()).collect(Collectors.toSet());
        List<Personne> clients = personneService.recupererClients();

        clients.stream().forEach(client -> {
            List<Echeancier> facturesParClient = echeancierService.getEcheanciersByPersonne(client.getIdPersonne(), null);

            /*BigDecimal montantSemaineEnCours =  facturesParClient.stream()
                    .filter(f -> !(DateUtils.toLocalDate(f.getDateFacture()).isAfter(finSemaine)
                            || DateUtils.toLocalDate(f.getDateFacture()).isBefore(debutSemaine)))
                    .map(f -> f.getResteAPayer())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);*/
            BigDecimal montantSemaineEnCours = echeancierService.
                    recupererAchatsClientParDate(client, DateUtils.asDate(debutSemaine), DateUtils.asDate(finSemaine));

            /*BigDecimal montantSemaine_1 =  facturesParClient.stream()
                    .filter(f -> !(DateUtils.toLocalDate(f.getDateFacture()).isAfter(finSemaine_1)
                            || DateUtils.toLocalDate(f.getDateFacture()).isBefore(debutSemaine_1)))
                    .map(f -> f.getResteAPayer())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);*/
            BigDecimal montantSemaine_1 = echeancierService.
                    recupererAchatsClientParDate(client, DateUtils.asDate(debutSemaine_1), DateUtils.asDate(finSemaine_1));

            /*BigDecimal montantSemaine_2 =  facturesParClient.stream()
                    .filter(f -> !(DateUtils.toLocalDate(f.getDateFacture()).isAfter(finSemaine_2)
                            || DateUtils.toLocalDate(f.getDateFacture()).isBefore(debutSemaine_2)))
                    .map(f -> f.getResteAPayer())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);*/
            BigDecimal montantSemaine_2 = echeancierService.
                    recupererAchatsClientParDate(client, DateUtils.asDate(debutSemaine_2), DateUtils.asDate(finSemaine_2));

            BigDecimal retard =  facturesParClient.stream()
                    .filter(f -> DateUtils.toLocalDate(f.getDateFacture()).isBefore(debutSemaine_2))
                    .map(f -> f.getResteAPayer())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // TODO Au lieu de manipuler toute la liste 'facturesParClient' il vaut mieux
            //  additionner 'retard', 'montantSemaine_2', 'montantSemaine_1' et 'montantSemaineEnCours'
            BigDecimal montantSolde =  facturesParClient.stream()
                    .map(f -> f.getResteAPayer())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            /*BigDecimal montantSolde = montantSemaineEnCours.add(montantSemaine_1.add(montantSemaine_2.add(retard)));*/


            BigDecimal seuil = calculerSeuilClient(client, date, montantSolde);

            BigDecimal montantAchatsSEnCours = echeancierService.recupererAchatsClientParDate(client, DateUtils.asDate(debutSemaine), DateUtils.asDate(finSemaine));

            BigDecimal montantPayeSEnCours = paiementService.recupererPaiementsClientParDate(client, DateUtils.asDate(debutSemaine), DateUtils.asDate(finSemaine));

            NoteClient noteClient = noterClient(montantSemaineEnCours, montantSemaine_1, montantSemaine_2,
                    retard, montantSolde, montantAchatsSEnCours, montantPayeSEnCours, seuil, client, date);

            HistoriqueClient historiqueClient = new HistoriqueClient("S_" + DateUtils.getWeekNumberOfYear(date) + "_" + DateUtils.toLocalDate(new Date()).getYear(),
                    client.getNomPersonne(), montantSemaineEnCours, montantSemaine_1, montantSemaine_2, retard,
                    montantSolde,montantAchatsSEnCours,montantPayeSEnCours,seuil, noteClient, true);

            historiqueClientRepository.save(historiqueClient);
        });
    }

    @Override
    public Map<Integer, NoteClient> get6LastNotes(String nomClient, Date date) {
        Map<Integer, NoteClient> result = new HashMap<>();
        int numeroSemaine = DateUtils.getWeekNumberOfYear(date);
        for(int i=0; i< 6; i++) {
            String semaineID = "S_" + numeroSemaine + "_" + DateUtils.toLocalDate(new Date()).getYear();
            NoteClient note = historiqueClientRepository.recupererNoteClientParSemaine(nomClient, semaineID);
            result.put(numeroSemaine, note);
            numeroSemaine -= 1;
        }
        return result;
    }

    private BigDecimal calculerSeuilClient(Personne client, Date date, BigDecimal solde) {
        if(solde.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        BigDecimal seuil = recupererSeuilClientParSemaine_1(client, date);
        if(seuil == null) {
            return solde;
        }
        return seuil;
    }

    private NoteClient noterClient(BigDecimal montantSemaineEnCours, BigDecimal montantSemaine_1, BigDecimal montantSemaine_2,
                               BigDecimal retard, BigDecimal montantSolde, BigDecimal montantAchatsSEnCours,
                               BigDecimal montantPayeSEnCours, BigDecimal seuil, Personne client, Date date) {


        BigDecimal percentage = BigDecimal.ZERO;
        if(montantSolde.compareTo(BigDecimal.ZERO) > 0) {
            percentage = (retard.divide(montantSolde, RoundingMode.HALF_UP)).multiply(new BigDecimal("100"));
        }
        /**
         * Si le ration retard/soldeTotal est inférieur à la marge tolérable, on evite un mauvais classement au client.
         */
        if (percentage.compareTo(new BigDecimal(MARGE_TOLERABLE)) <= 0) {
            if(montantSemaine_2.compareTo(BigDecimal.ZERO) > 0) {
                //StringBuilder note = new StringBuilder("NORMAL");
                if(montantSemaineEnCours.compareTo(BigDecimal.ZERO) <= 0 && montantSemaine_1.compareTo(BigDecimal.ZERO) <= 0) {
                    return NoteClient.NORMAL_AVEC_URGENCE;
                    //note.append(" + URGENCE COMM");
                } else if(montantSemaineEnCours.compareTo(BigDecimal.ZERO) == 0) {
                    return NoteClient.NORMAL_AVEC_ALERTE;
                    //note.append(" + ALERTE COMM");
                } else {
                    return NoteClient.NORMAL;
                }
                //return note.toString();
            } else if(montantSemaine_1.compareTo(BigDecimal.ZERO) > 0) {
                //StringBuilder note = new StringBuilder("BON CLIENT");
                if(montantSemaineEnCours.compareTo(BigDecimal.ZERO) <= 0) {
                    return NoteClient.BON_CLIENT_AVEC_URGENCE;
                    //note.append(" + URGENCE COMM");
                }
                //return note.toString();
                return NoteClient.BON_CLIENT;
            } else if(montantSemaineEnCours.compareTo(BigDecimal.ZERO) > 0) {
                //return "TRES BON CLIENT";
                return NoteClient.TRES_BON_CLIENT;
            } else if(montantPayeSEnCours.compareTo(BigDecimal.ZERO) > 0 && montantAchatsSEnCours.compareTo(BigDecimal.ZERO) > 0) {
                //return "ELITE";
                return NoteClient.ELITE;
            } else {
                //return "CLIENT INACTIF SANS ARRIERES";
                return NoteClient.INACTIF_SANS_ARRIERES;
            }
        } else {
            if(retard.compareTo(seuil) > 0 || montantSemaineEnCours.compareTo(montantPayeSEnCours) > 0 ) {
                //return "CLIENT DANGEREUX";
                return NoteClient.DANGEREUX;
            } else if(montantSemaineEnCours.add(montantSemaine_1.add(montantSemaine_2)) == BigDecimal.ZERO) {
                //return "CLIENT INACTIF AVEC ARRIERES";
                return NoteClient.INACTIF_AVEC_ARRIERES;
            } else {
                //return "MAUVAIS";
                return NoteClient.MAUVAIS;
            }
        }

    }

    private BigDecimal recupererSeuilClientParSemaine_1(Personne client, Date date) {
        String semaineID = "S_" + (DateUtils.getWeekNumberOfYear(date) - 1 )+ "_" + DateUtils.toLocalDate(new Date()).getYear();
        String nomClient = client.getNomPersonne();
        return historiqueClientRepository.recupererSeuilClientParSemaine(nomClient, semaineID);
    }
}
