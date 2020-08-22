package ma.sid.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private Long id;
    private String nomRole;
    @ManyToMany(fetch= FetchType.EAGER, cascade=CascadeType.ALL)
    private Set<EcranAutorise> ecranAutorise = new HashSet<>();

    public void addEcransAutorises(Set<EcranAutorise> ecrans) {
        this.ecranAutorise.clear();
        this.ecranAutorise.addAll(ecrans);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", nomRole=" + nomRole +
                ", ecranAutorise=" + ecranAutorise +
                "}";
    }
}
