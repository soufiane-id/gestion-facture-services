package ma.sid.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String nom;
    private String prenom;
    @ManyToMany(fetch= FetchType.EAGER)
    private Collection<Role> roles=new ArrayList<>();
}
