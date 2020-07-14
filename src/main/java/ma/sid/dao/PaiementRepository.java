package ma.sid.dao;

import ma.sid.entities.Paiement;
import ma.sid.entities.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findAllByPersonneAndDatePaiementGreaterThanEqualAndDatePaiementLessThanEqual(Personne client, Date debut, Date fin);
}
