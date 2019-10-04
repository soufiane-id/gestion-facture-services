package ma.sid.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ma.sid.entities.Societe;

@RepositoryRestResource
public interface SocieteRepository extends JpaRepository<Societe, Long> {

}
