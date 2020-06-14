package ma.sid.services;

import ma.sid.dao.PaiementRepository;
import ma.sid.entities.Paiement;
import ma.sid.entities.Personne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PaiementServiceImpl implements PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;


    @Override
    public BigDecimal recupererPaiementsClientParDate(Personne client, Date startWeek, Date endWeek) {
        List<Paiement> paiementList =  paiementRepository.findAllByPersonneAndDatePaiementGreaterThanEqualAndDatePaiementLessThanEqual(client,
                startWeek, endWeek);

        return paiementList.stream().map(p -> p.getMontant()).reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
