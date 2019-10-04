package ma.sid.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reglement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeReglement;
	private TypeReglement typeReglement;
	private BigDecimal montantReglement;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLIENT_ID", nullable = true)
	private Client client;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOURNISSEUR_ID", nullable = true)
	private Fournisseur fournisseur;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOCIETE_ID", nullable = false)
	private Societe societe;
}
