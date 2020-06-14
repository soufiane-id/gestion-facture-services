package ma.sid.services;

import ma.sid.entities.Banque;

import java.math.BigDecimal;
import java.util.List;

public interface BanqueService {

    List<Banque> recupererBanques();

    void updateStock(BigDecimal nouveauStock);
}
