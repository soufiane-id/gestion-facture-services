package ma.sid.web;

import ma.sid.entities.Utilisateur;
import ma.sid.services.UtilisateurRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class UtilisateurRoleController {
    private UtilisateurRoleService utilisateurService;

    public UtilisateurRoleController(UtilisateurRoleService utilisateurService){
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/listUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(utilisateurService.getAllUsers());
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Utilisateur> updateUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.updateUser(utilisateur));
    }

    @GetMapping("/listRoles")
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(utilisateurService.getAllRoles());
    }

    @GetMapping("/roleByName")
    public ResponseEntity<?> getRoleByName(String roleName) {
        return ResponseEntity.ok(utilisateurService.getRoleByName(roleName));
    }

    @PostMapping("/role")
    public ResponseEntity<?> addRole(@RequestBody String nomRole) {
        return ResponseEntity.ok(utilisateurService.addRole(nomRole));
    }

    @GetMapping("/ecransByRole")
    public ResponseEntity<?> getEcransByRoleName(Long roleId) {
        return ResponseEntity.ok(utilisateurService.getEcransByRoleName(roleId));
    }

    @PostMapping("/addEcransToRole/{roleId}")
    public ResponseEntity<?> addEcransToRole(@PathVariable Long roleId, @RequestBody Set<String> ecrans) {
        return ResponseEntity.ok(utilisateurService.addEcransToRole(roleId, ecrans));
    }

}
