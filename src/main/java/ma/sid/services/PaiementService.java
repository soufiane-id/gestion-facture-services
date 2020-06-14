package ma.sid.services;

import ma.sid.dao.PaiementRepository;
import ma.sid.entities.Personne;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

public interface PaiementService {

    public BigDecimal recupererPaiementsClientParDate(Personne client, Date startWeek, Date endWeek);

}
