package ma.sid.dao;

import ma.sid.entities.OperationBancaire;
import ma.sid.dto.enums.StatutOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface OperationBancaireRepository extends JpaRepository<OperationBancaire, Long> {

    /*@Query("SELECT op FROM OperationBancaire op WHERE op.statutOperation = 'A_TRAITER' ")
    List<OperationBancaire> findAllOperationsATraiter();

    @Query("SELECT op FROM OperationBancaire op WHERE op.statutOperation = 'VALIDE' ")
    List<OperationBancaire> findAllOperationsValides();*/

    List<OperationBancaire> findAllByStatutOperation(StatutOperation statutOperation);

    List<OperationBancaire> findAllByStatutOperationIn(List<StatutOperation> status);


}
