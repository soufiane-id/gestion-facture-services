package ma.sid.web;

import ma.sid.entities.Banque;
import ma.sid.entities.Echeancier;
import ma.sid.exceptions.PersonneNotFoundException;
import ma.sid.services.BanqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

//@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class BanqueController {

    private BanqueService banqueService;

    @Autowired
    public BanqueController(BanqueService banqueService){
        this.banqueService = banqueService;
    }

    @SuppressWarnings("unused")
    @GetMapping("/listBanque")
    public List<Banque> findAll() {
        return banqueService.recupererBanques();
    }

    @PostMapping("/updateStock")
    public void updateStock(@RequestBody BigDecimal nouveauStock) {
        banqueService.updateStock(nouveauStock);
    }

}
