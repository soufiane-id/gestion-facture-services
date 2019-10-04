package ma.sid.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ma.sid.entities.OperationBancaire;

@RepositoryRestResource
public interface OperationBancaireRepository extends JpaRepository<OperationBancaire, Long> {

}
