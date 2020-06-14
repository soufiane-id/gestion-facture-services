package ma.sid.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONNE_ID")
    private Personne personne;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOCIETE_ID", nullable = false)
    private Societe societe;
    private BigDecimal montant;
    private Date datePaiement;
}
