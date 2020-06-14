package ma.sid.dto;

import lombok.Data;
import ma.sid.entities.OperationBancaire;

import java.math.BigDecimal;

@Data
public class ChargeFixe {
    private Long idSociete;
    private OperationBancaire operationBancaire;
    private BigDecimal montant;
}
