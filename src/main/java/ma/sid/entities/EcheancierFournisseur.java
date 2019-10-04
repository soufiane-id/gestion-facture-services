package ma.sid.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EcheancierFournisseur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEcheancierFournisseur;
	private Date dateFacture;
	private Date dateEcheance;
	private Double montantFacture;
	private Double montantPaye;
	private Double resteAPayer;
	private Date dateReglementFacture;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOURNISSEUR_ID", nullable = false)
	private Fournisseur fournisseur;
	
}
