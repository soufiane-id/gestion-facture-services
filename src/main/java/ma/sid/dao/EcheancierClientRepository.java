package ma.sid.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ma.sid.entities.EcheancierClient;

@CrossOrigin( origins = "http://localhost:8081" )
@RepositoryRestResource
public interface EcheancierClientRepository extends JpaRepository<EcheancierClient, Long> {

}
