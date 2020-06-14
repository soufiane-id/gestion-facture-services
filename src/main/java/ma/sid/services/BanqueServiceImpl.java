package ma.sid.services;

import ma.sid.dao.BanqueRepository;
import ma.sid.entities.Banque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BanqueServiceImpl implements BanqueService{
    @Autowired
    private BanqueRepository banqueRepository;

    /**
     * récupère toutes les banques disponibles.
     * @return List<Banque>
     */
    @Override
    public List<Banque> recupererBanques() {
        return banqueRepository.findAll();
    }

    @Override
    public void updateStock(BigDecimal nouveauStock) {
        Banque banque = banqueRepository.getOne(1L); // a supposer que 1 correspond à Teralal.
        banque.setMontantStock(nouveauStock);
        banqueRepository.save(banque);
    }
}
