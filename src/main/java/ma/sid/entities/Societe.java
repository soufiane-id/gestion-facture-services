package ma.sid.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

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
