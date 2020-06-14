package ma.sid.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.sid.dto.enums.NoteClient;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity @IdClass(HistoriqueClientId.class)
public class HistoriqueClient implements Serializable {
    @Id
    private String semaineId;
    @Id
    private String nomClient;
    private BigDecimal montantSemaineEnCours;
    private BigDecimal montantSemaine_1;
    private BigDecimal montantSemaine_2;
    private BigDecimal retard;
    private BigDecimal montantSolde;
    private BigDecimal montantAchatsSemaineEnCours;
    private BigDecimal montantPayeSemaineEnCours;
    private BigDecimal seuil;
    @Enumerated(value = EnumType.STRING)
    private NoteClient noteClient;
    private Boolean isSynchronized;

    public HistoriqueClient(String semaineId, String nomClient, BigDecimal montantSemaineEnCours, BigDecimal montantSemaine_1, BigDecimal montantSemaine_2, BigDecimal retard, BigDecimal montantSolde, BigDecimal montantAchatsSemaineEnCours, BigDecimal montantPayeSemaineEnCours, NoteClient noteClient, Boolean isSynchronized) {
        this.semaineId = semaineId;
        this.nomClient = nomClient;
        this.montantSemaineEnCours = montantSemaineEnCours;
        this.montantSemaine_1 = montantSemaine_1;
        this.montantSemaine_2 = montantSemaine_2;
        this.retard = retard;
        this.montantSolde = montantSolde;
        this.montantAchatsSemaineEnCours = montantAchatsSemaineEnCours;
        this.montantPayeSemaineEnCours = montantPayeSemaineEnCours;
        this.noteClient = noteClient;
        this.isSynchronized = isSynchronized;
    }
}