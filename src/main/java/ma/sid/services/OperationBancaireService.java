package ma.sid.services;

import ma.sid.entities.OperationBancaire;

import java.util.List;

public interface OperationBancaireService {
    List<OperationBancaire> recupererOperationsBancairesValides();

    List<OperationBancaire> recupererOperationsBancairesATraiter();

    List<OperationBancaire> recupererOperationsBancaires();

    OperationBancaire ajouterOperationBancaire(OperationBancaire operation);

    List<OperationBancaire> integererOperations(List<OperationBancaire> operations);

    List<OperationBancaire> recupererTransfertsArgent();

    List<OperationBancaire> recupererOperationEnAttenteFacture();

    List<OperationBancaire> recupererOperationChargesFixes();

    List<OperationBancaire> recupererOperationImpayes();
}
