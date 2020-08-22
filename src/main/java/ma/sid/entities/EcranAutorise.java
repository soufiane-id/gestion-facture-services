package ma.sid.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EcranAutorise {
    @Id
    @GeneratedValue
    private Long idEcran;
    private String nomEcran;

    @Override
    public String toString() {
        return "EcranAutorise{" +
                "idEcran=" + idEcran +
                ", nomEcran=" + nomEcran  +
                "}";
    }
}
