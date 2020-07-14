package ma.sid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    private String nom;
    private String prenom;
    private String username;
    private String password;
    private String repassword;
}
