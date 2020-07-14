package ma.sid.dao;

import ma.sid.entities.Societe;
import org.springframework.data.jpa.repository.JpaRepository;

//@RepositoryRestResource
public interface SocieteRepository extends JpaRepository<Societe, Long> {

}
