package ma.sid.web;

import ma.sid.dto.RegisterForm;
import ma.sid.entities.Utilisateur;
import ma.sid.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private UtilisateurService utilisateurService;

    @Autowired
    public AuthController(UtilisateurService utilisateurService){
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/register")
    public Utilisateur registerUser(@RequestBody RegisterForm registerForm) {
        return utilisateurService.registerUser(registerForm);
    }
}
