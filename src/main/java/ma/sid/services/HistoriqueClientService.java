package ma.sid.services;

import ma.sid.dto.enums.NoteClient;
import ma.sid.entities.HistoriqueClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface HistoriqueClientService {
    List<HistoriqueClient> getHistoriqueClient(Date date, boolean refreshMode);

    void synchronize(Date date);

    Map<Integer, String> get6LastNotes(String nomClient, Date date);
}
