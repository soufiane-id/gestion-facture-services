package ma.sid.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ma.sid.dto.enums.MoyenPaiement;
import ma.sid.dto.enums.StatutIntegration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Echeancier implements Comparable<Echeancier> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEcheancier;
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dateFacture;
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dateEcheance;
	@NotNull
	private String numeroDocument;
	private String nomPersonne;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSONNE_ID", nullable = false)
	private Personne personne;
	private String codeTiers;
	private MoyenPaiement moyenPaiement;
	private String soldee;
	private BigDecimal montantFacture;
	private BigDecimal montantPaye;
	private BigDecimal resteAPayer;
	@ElementCollection
	@CollectionTable(name="FACTURE_OPERATION", joinColumns=@JoinColumn(name="idEcheancier"))
	@Column(name="operations")
	private Set<Long> operations;
	@JsonFormat(pattern="dd/MM/yy")
	private Date dateReglementFacture;
	@Transient
	private StatutIntegration statutIntegration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOCIETE_ID", nullable = false)
	private Societe societe;
	
	public Echeancier(Long idEcheancier, Date dateFacture, Date dateEcheance, String numeroDocument,
                      Personne personne, String codeTiers, MoyenPaiement moyenPaiement, String soldee, BigDecimal montantFacture,
                      BigDecimal montantPaye, BigDecimal resteAPayer, Date dateReglementFacture, Societe societe) {
		super();
		this.idEcheancier = idEcheancier;
		this.dateFacture = dateFacture;
		this.dateEcheance = dateEcheance;
		this.numeroDocument = numeroDocument;
		this.personne = personne;
		this.codeTiers = codeTiers;
		this.moyenPaiement = moyenPaiement;
		this.soldee = soldee;
		this.montantFacture = montantFacture;
		this.montantPaye = montantPaye;
		this.resteAPayer = resteAPayer;
		this.dateReglementFacture = dateReglementFacture;
		this.societe = societe;
	}

	public Echeancier(Long idEcheancier, Date dateFacture, Date dateEcheance, String numeroDocument,
					  Personne personne, String nomPersonne, String codeTiers, MoyenPaiement moyenPaiement, String soldee, BigDecimal montantFacture,
					  BigDecimal montantPaye, BigDecimal resteAPayer, Date dateReglementFacture, Societe societe) {
		super();
		this.idEcheancier = idEcheancier;
		this.dateFacture = dateFacture;
		this.dateEcheance = dateEcheance;
		this.numeroDocument = numeroDocument;
		this.personne = personne;
		this.nomPersonne = nomPersonne;
		this.codeTiers = codeTiers;
		this.moyenPaiement = moyenPaiement;
		this.soldee = soldee;
		this.montantFacture = montantFacture;
		this.montantPaye = montantPaye;
		this.resteAPayer = resteAPayer;
		this.dateReglementFacture = dateReglementFacture;
		this.societe = societe;
	}

	@Override
	public int compareTo(Echeancier o) {
		return this.dateFacture.compareTo(o.getDateFacture());
	}
}
