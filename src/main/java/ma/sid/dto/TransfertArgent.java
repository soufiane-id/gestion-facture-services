package ma.sid.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransfertArgent {
    private Long codeOperation;
    private Long banqueFrom;
    private Long banqueTo;
    private BigDecimal montant;
}
