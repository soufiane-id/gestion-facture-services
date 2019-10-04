package ma.sid.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class OperationBancaire {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codeOperation;
	@JsonFormat(pattern="dd/MM/yy")
	private Date dateOperation;
	private String nomOperation;
	private BigDecimal montantDebit;
	private BigDecimal montantCredit;
	private StatutOperation statutOperation;
	
	
	
}
