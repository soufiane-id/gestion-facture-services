package ma.sid.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EcheancierClient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEcheancierClient;
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dateFacture;
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dateEcheance;
	private String numeroDocument;
	private String nomClient;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLIENT_ID", nullable = false)
	private Client client;
	private String codeTiers;
	private MoyenPaiement moyenPaiement;
	private String soldee;
	private BigDecimal montantFacture;
	private BigDecimal montantPaye;
	private BigDecimal resteAPayer;
	@JsonFormat(pattern="dd/MM/yy")
	private Date dateReglementFacture;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOCIETE_ID", nullable = false)
	private Societe societe;

	public EcheancierClient(Long idEcheancierClient, Date dateFacture, Date dateEcheance, String numeroDocument,
			Client client, String codeTiers, MoyenPaiement moyenPaiement, String soldee, BigDecimal montantFacture,
			BigDecimal montantPaye, BigDecimal resteAPayer, Date dateReglementFacture, Societe societe) {
		super();
		this.idEcheancierClient = idEcheancierClient;
		this.dateFacture = dateFacture;
		this.dateEcheance = dateEcheance;
		this.numeroDocument = numeroDocument;
		this.client = client;
		this.codeTiers = codeTiers;
		this.moyenPaiement = moyenPaiement;
		this.soldee = soldee;
		this.montantFacture = montantFacture;
		this.montantPaye = montantPaye;
		this.resteAPayer = resteAPayer;
		this.dateReglementFacture = dateReglementFacture;
		this.societe = societe;
	}
	
	
	
}
