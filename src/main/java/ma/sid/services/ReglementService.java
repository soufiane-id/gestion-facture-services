package ma.sid.services;

import ma.sid.dto.ChargeFixe;
import ma.sid.dto.DifferenceReglement;
import ma.sid.entities.Echeancier;
import ma.sid.dto.TransfertArgent;

import java.math.BigDecimal;
import java.util.List;

public interface ReglementService {

    void reglerOperationBancaireParVirement(List<Echeancier> echeanciers, Long codeOperation, BigDecimal montantFactureSelectionne);

    void reglerOperationsParCheque(List<Echeancier> echeanciers, Long codeOperation, BigDecimal montantFacture, BigDecimal montantFactureSelectionne);

    void reglerTransfertArgent(TransfertArgent transfertArgent);

    void reglerPaiementSansFacture(DifferenceReglement differenceReglement);

    void reglerChargesFixes(ChargeFixe chargeFixe);

    void reglerImpaye(List<Echeancier> echeanciers, Long codeOperation);
}
