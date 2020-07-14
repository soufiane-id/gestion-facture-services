package ma.sid.services;

import ma.sid.dto.enums.RoleEnum;
import ma.sid.entities.Role;
import ma.sid.entities.Utilisateur;

public interface UtilisateurService {
    Utilisateur saveUser(Utilisateur u);
    Role saveRole(Role r);
    Utilisateur findUserByUsername(String username);
    void addRoleToUser(String username, RoleEnum role);
}
