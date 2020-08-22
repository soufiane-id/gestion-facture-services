package ma.sid.services;

import ma.sid.entities.Role;
import ma.sid.entities.Utilisateur;

import java.util.List;
import java.util.Set;

public interface UtilisateurRoleService {
    Utilisateur saveUser(Utilisateur u);
    Role saveRole(Role r);
    Utilisateur findUserByUsername(String username);
    void addRoleToUser(String username, String role);
    List<Utilisateur> getAllUsers();
    List<Role> getAllRoles();
    Utilisateur updateUser(Utilisateur utilisateur);
    List<String> getEcransByRoleName(Long roleName);
    Role getRoleByName(String roleName);
    Role addEcransToRole(Long roleId, Set<String> ecrans);
    Role addRole(String nomRole);
}
