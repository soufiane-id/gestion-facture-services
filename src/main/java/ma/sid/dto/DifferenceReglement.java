package ma.sid.dto;

import lombok.Data;
import ma.sid.entities.OperationBancaire;

import java.math.BigDecimal;

@Data
public class DifferenceReglement {
    private Long idClient;
    private Long idSociete;
    private BigDecimal montantFacture;
    private OperationBancaire operationBancaire;
}
