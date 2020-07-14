package ma.sid.web;

import ma.sid.dto.RegisterForm;
import ma.sid.dto.enums.RoleEnum;
import ma.sid.entities.Utilisateur;
import ma.sid.payload.request.LoginRequest;
import ma.sid.payload.response.JwtResponse;
import ma.sid.payload.response.MessageResponse;
import ma.sid.security.jwt.JwtUtils;
import ma.sid.security.services.UserDetailsImpl;
import ma.sid.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    private UtilisateurService utilisateurService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    public AuthController(UtilisateurService utilisateurService){
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterForm registerForm) {
        String username = registerForm.getUsername();
        Utilisateur user = utilisateurService.findUserByUsername(username);
        if(user != null)
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Utilisateur existe déjà!"));
        String password = registerForm.getPassword();
        String repassword = registerForm.getRepassword();
        if(!password.equals(repassword))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Les mots de passe ne sont pas identiques!"));
        Utilisateur u = new Utilisateur();
        u.setUsername(username);
        u.setPassword(password);
        u.setNom(registerForm.getNom());
        u.setPrenom(registerForm.getPrenom());
        utilisateurService.saveUser(u);
        utilisateurService.addRoleToUser(username, RoleEnum.USER);
        return ResponseEntity.ok(new MessageResponse("Utilisateur enregistré avec succès!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getNom(),
                userDetails.getPrenom(),
                userDetails.getUsername(),
                roles));
    }
}
