package ma.sid.dao;

import ma.sid.entities.EcranAutorise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcranAutoriseRepository extends JpaRepository<EcranAutorise, Long> {

    EcranAutorise findByNomEcran(String nomEcran);

}
