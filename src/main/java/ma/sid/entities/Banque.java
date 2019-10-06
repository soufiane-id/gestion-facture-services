package ma.sid.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Banque {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idBanque;
	private String nomBanque;
	private BigDecimal soldeDebit;
	private BigDecimal soldeCredit;
}
