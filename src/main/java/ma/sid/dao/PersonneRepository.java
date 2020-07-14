package ma.sid.dao;

import ma.sid.entities.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@RepositoryRestResource
public interface PersonneRepository extends JpaRepository<Personne, Long> {
	
	@Query("SELECT p FROM Personne p WHERE p.typePersonne = 'CLIENT' ")
	List<Personne> findAllClients();
	
	@Query("SELECT p FROM Personne p WHERE p.typePersonne = 'FOURNISSEUR' ")
	List<Personne> findAllFournisseurs();

	Personne findByNomPersonne(String nomPersonne);
	

}
