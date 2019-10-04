package ma.sid.entities;

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
public class Societe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSociete;
	private String nomSociete;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANQUE_ID", nullable = false)
	private Banque banque;
}
