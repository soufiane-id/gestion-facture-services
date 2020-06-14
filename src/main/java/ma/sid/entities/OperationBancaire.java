package ma.sid.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import ma.sid.dto.enums.StatutIntegration;
import ma.sid.dto.enums.StatutOperation;
import ma.sid.dto.enums.TypeOperation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OperationBancaire {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeOperation;
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date dateOperation;
	private String nomOperation;
	private BigDecimal montantDebit;
	private BigDecimal montantCredit;
	@Enumerated(value = EnumType.STRING)
	private StatutOperation statutOperation;
	@Enumerated(value = EnumType.STRING)
	private TypeOperation typeOperation;
	@Transient
	private StatutIntegration statutIntegration;

}
