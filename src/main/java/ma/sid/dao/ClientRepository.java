package ma.sid.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ma.sid.entities.Client;

@CrossOrigin( origins = "http://localhost:8081" )
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

}
