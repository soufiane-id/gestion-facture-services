package ma.sid.web;

import ma.sid.entities.Societe;
import ma.sid.services.SocieteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class SocieteController {

    private SocieteService societeService;

    @Autowired
    public SocieteController(SocieteService societeService){
        this.societeService = societeService;
    }

    @SuppressWarnings("unused")
    @GetMapping("/listSociete")
    public List<Societe> findAll() {
        return societeService.recupererSocietes();
    }
    
}
