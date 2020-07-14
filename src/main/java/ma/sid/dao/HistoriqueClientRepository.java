package ma.sid.dao;

import ma.sid.dto.enums.NoteClient;
import ma.sid.entities.HistoriqueClient;
import ma.sid.entities.HistoriqueClientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface HistoriqueClientRepository extends JpaRepository<HistoriqueClient, HistoriqueClientId> {

    List<HistoriqueClient> findBySemaineId(String semaineId);

    @Query("SELECT hist.seuil FROM HistoriqueClient hist WHERE hist.nomClient = :nomClient" +
            " AND hist.semaineId = :semaineId")
    BigDecimal recupererSeuilClientParSemaine(@Param("nomClient") String nomClient, @Param("semaineId") String semaineId);

    @Query("SELECT hist.noteClient FROM HistoriqueClient hist WHERE hist.nomClient = :nomClient" +
            " AND hist.semaineId = :semaineId")
    NoteClient recupererNoteClientParSemaine(@Param("nomClient") String nomClient, @Param("semaineId") String semaineId);
}
