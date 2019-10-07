package ma.sid.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import ma.sid.entities.EcheancierClient;

@CrossOrigin( origins = "http://localhost:8081" )
@RepositoryRestResource
public interface EcheancierClientRepository extends JpaRepository<EcheancierClient, Long> {

	@Query("SELECT ech FROM EcheancierClient ech WHERE ech.resteAPayer != 0")
	List<EcheancierClient> findAllEcheancesNonRegles();
	
	@Query("SELECT ech FROM EcheancierClient ech WHERE ech.resteAPayer = 0")
	List<EcheancierClient> findAllEcheancesRegles();
	
	@Query("SELECT ech FROM EcheancierClient ech WHERE ech.client.idClient = :idClient AND ech.resteAPayer != 0")
	List<EcheancierClient> getEcheanciersByClient(@Param("idClient") Long idClient);
}
