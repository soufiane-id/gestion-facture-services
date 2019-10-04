package ma.sid.entities;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class AbstractEcheancierEntity {

	private Date dateFacture;
	private Date dateEcheance;
	private Double montantFacture;
	private Double montantPaye;
	private Double resteAPayer;
	private Date dateReglementFacture;
}
