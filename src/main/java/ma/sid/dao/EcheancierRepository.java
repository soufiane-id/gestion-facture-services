package ma.sid.dao;

import ma.sid.entities.Echeancier;
import ma.sid.entities.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;
import java.util.Set;

@CrossOrigin( origins = "http://localhost:8081" )
//@RepositoryRestResource
public interface EcheancierRepository extends JpaRepository<Echeancier, Long> {

	@Query("SELECT ech FROM Echeancier ech WHERE ech.resteAPayer = 0")
	List<Echeancier> findAllEcheancesRegles();

	@Query("SELECT ech FROM Echeancier ech WHERE ech.resteAPayer != 0 AND ech.personne.typePersonne = 'CLIENT' ")
	List<Echeancier> findAllEcheancesClientNonRegles();

	@Query("SELECT ech FROM Echeancier ech WHERE ech.resteAPayer = 0 AND ech.personne.typePersonne = 'CLIENT'")
	List<Echeancier> findAllEcheancesClientRegles();

	@Query("SELECT ech FROM Echeancier ech WHERE ech.resteAPayer != 0 AND ech.personne.typePersonne = 'FOURNISSEUR' ")
	List<Echeancier> findAllEcheancesFournisseurNonRegles();

	@Query("SELECT ech FROM Echeancier ech WHERE ech.resteAPayer = 0 AND ech.personne.typePersonne = 'FOURNISSEUR'")
	List<Echeancier> findAllEcheancesFournisseurRegles();

	/**
	 * Retourne la liste des factures d'une personne en fonction ( en option ) de la societe
	 * @param idPersonne
	 * @param idSociete si null, on l'ignore et on retourne le resultat de toutes les societes.
	 * @return
	 */
	@Query("SELECT ech FROM Echeancier ech WHERE ech.personne.idPersonne = :idPersonne " +
			"AND ech.resteAPayer <> 0 AND (:idSociete is null or ech.societe.idSociete = :idSociete) ")
	List<Echeancier> getEcheanciersByPersonne(@Param("idPersonne") Long idPersonne, @Param("idSociete") Long idSociete);
	
	@Query("SELECT ech FROM Echeancier ech WHERE ech.personne.typePersonne = 'CLIENT' ")
	List<Echeancier> findAllEcheancesClient();
	//FIXME
	List<Echeancier> findAllByOperations(Set<Long> operations);
	
	//List<Echeancier> findAllEcheancesClient(TypePersonne typePersonne);

	List<Echeancier> findByNumeroDocument(String numeroDocument);

	List<Echeancier> findAllByPersonneAndDateFactureGreaterThanEqualAndDateFactureLessThanEqual(Personne client, Date startWeek, Date endWeek);
	
}
