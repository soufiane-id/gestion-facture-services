package ma.sid.dao;

import ma.sid.entities.Banque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BanqueRepository extends JpaRepository<Banque, Long> {

}
