package ma.sid.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoriqueClientId implements Serializable {
    String semaineId;
    String nomClient;

}