package ma.sid.web;

import ma.sid.dto.enums.NoteClient;
import ma.sid.entities.HistoriqueClient;
import ma.sid.services.HistoriqueClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

//@CrossOrigin( origins = "http://localhost:8081" )
@RestController
public class HistoriquePaiementClient {

    private HistoriqueClientService historiqueClientService;

    @Autowired
    public HistoriquePaiementClient(HistoriqueClientService historiqueClientService){
        this.historiqueClientService = historiqueClientService;
    }


    @GetMapping("/getHistoriqueClient")
    public List<HistoriqueClient> getHistoriqueClient(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date, boolean refreshMode) {
        return historiqueClientService.getHistoriqueClient(date, refreshMode);
    }

    @PostMapping("/synchronize")
    public void synchronize(@RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date) {
        historiqueClientService.synchronize(date);
    }

    @GetMapping("/lastNotesClient")
    public Map<Integer, String> get6LastNotes(String nomClient,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date) {
        return historiqueClientService.get6LastNotes(nomClient, date);
    }

}
