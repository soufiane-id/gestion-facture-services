package ma.sid.services;

import ma.sid.dao.OperationBancaireRepository;
import ma.sid.entities.OperationBancaire;
import ma.sid.dto.enums.StatutIntegration;
import ma.sid.dto.enums.StatutOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OperationBancaireServiceImpl implements OperationBancaireService{

    @Autowired
    private OperationBancaireRepository operationBancaireRepository;

    @Override
    public List<OperationBancaire> recupererOperationsBancaires() {
        return operationBancaireRepository.findAll();
    }

    @Override
    public List<OperationBancaire> recupererOperationsBancairesValides() {
        return operationBancaireRepository.findAllByStatutOperation(StatutOperation.VALIDE);
    }

    @Override
    public List<OperationBancaire> recupererOperationsBancairesATraiter() {
        return operationBancaireRepository.findAllByStatutOperation(StatutOperation.A_TRAITER);
    }

    @Override
    public List<OperationBancaire> recupererTransfertsArgent() {
        return operationBancaireRepository.findAllByStatutOperation(StatutOperation.TRANSFERE);
    }

    @Override
    public List<OperationBancaire> recupererOperationEnAttenteFacture() {
        return operationBancaireRepository.findAllByStatutOperation(StatutOperation.EN_ATTENTE_FACTURE);
    }

    @Override
    public List<OperationBancaire> recupererOperationChargesFixes() {
        List<StatutOperation> status = new ArrayList<>();
        status.add(StatutOperation.TELEPHONE);
        status.add(StatutOperation.SALAIRE);
        status.add(StatutOperation.AUDIGEC);
        status.add(StatutOperation.GAZOIL);
        status.add(StatutOperation.POSTE);
        status.add(StatutOperation.FRAIS_BANCAIRES);
        status.add(StatutOperation.CHARGES_SOCIALES);
        return operationBancaireRepository.findAllByStatutOperationIn(status);
    }

    @Override
    public List<OperationBancaire> recupererOperationImpayes() {
        return operationBancaireRepository.findAllByStatutOperation(StatutOperation.IMPAYE);
    }

    @Override
    public void supprimerOperation(Long codeOperation) {
        operationBancaireRepository.deleteById(codeOperation);
    }

    @Override
    public OperationBancaire ajouterOperationBancaire(OperationBancaire operation) {
        operation.setStatutOperation(StatutOperation.A_TRAITER);
        return operationBancaireRepository.save(operation);
    }

    @Override
    public List<OperationBancaire> integererOperations(List<OperationBancaire> operations) {
        for(OperationBancaire op : operations) {
            try {
                ajouterOperationBancaire(op);
                op.setStatutIntegration(StatutIntegration.OK);
            } catch (Exception e) {
                op.setStatutIntegration(StatutIntegration.ERREUR);
            }
//			op.setStatutOperation(StatutOperation.A_TRAITER);
//			operationBancaireRepository.save(op);
        }
        return operations;
    }

}
