package ma.sid.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ma.sid.entities.EcheancierFournisseur;

@RepositoryRestResource
public interface EcheancierFournisseurRepository extends JpaRepository<EcheancierFournisseur, Long> {

}
