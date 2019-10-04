package ma.sid.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ma.sid.entities.Banque;

@RepositoryRestResource
public interface BanqueRepository extends JpaRepository<Banque, Long> {

}
