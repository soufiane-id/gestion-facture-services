package ma.sid.web;

import ma.sid.dto.ChargeFixe;
import ma.sid.dto.DifferenceReglement;
import ma.sid.entities.Echeancier;
import ma.sid.dto.TransfertArgent;
import ma.sid.services.ReglementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class ReglementController {

	private ReglementService reglementService;

	@Autowired
	public ReglementController(ReglementService reglementService) {
		this.reglementService = reglementService;
	}

	@PostMapping("/reglerOperations/{codeOperation}/{montantFactureSelectionne}")
	public void reglerOperationsBancairesParVirement(@RequestBody List<Echeancier> echeanciers, @PathVariable Long codeOperation, @PathVariable BigDecimal montantFactureSelectionne) {

		reglementService.reglerOperationBancaireParVirement(echeanciers, codeOperation, montantFactureSelectionne);

	}

	@PostMapping("/reglerOperationsByCheque/{codeOperation}/{montantFacture}/{montantFactureSelectionne}")
	public void reglerOperationsParCheque(@RequestBody List<Echeancier> echeanciers, @PathVariable Long codeOperation, @PathVariable BigDecimal montantFacture, @PathVariable BigDecimal montantFactureSelectionne) {
		reglementService.reglerOperationsParCheque(echeanciers, codeOperation, montantFacture, montantFactureSelectionne);
	}

	@PostMapping("/reglerSansFacture/")
	public void reglerPaiementSansFacture(@RequestBody DifferenceReglement differenceReglement) {
		reglementService.reglerPaiementSansFacture(differenceReglement);
	}

	@PostMapping("/reglerTransfertArgent/")
	public void reglerTransfertArgent(@RequestBody TransfertArgent transfertArgent) {
		reglementService.reglerTransfertArgent(transfertArgent);
	}

	@PostMapping("/reglerChargesFixes/")
	public void reglerChargesFixes(@RequestBody ChargeFixe chargeFixe) {
		reglementService.reglerChargesFixes(chargeFixe);
	}

	@PostMapping("/reglerImpaye/{codeOperation}")
	public void reglerImpaye(@RequestBody List<Echeancier> echeanciers, @PathVariable Long codeOperation) {
		reglementService.reglerImpaye(echeanciers, codeOperation);
	}

}
