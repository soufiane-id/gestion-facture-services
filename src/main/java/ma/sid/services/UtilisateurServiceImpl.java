package ma.sid.services;

import ma.sid.dao.RoleRepository;
import ma.sid.dao.UtilisateurRepository;
import ma.sid.dto.enums.RoleEnum;
import ma.sid.entities.Role;
import ma.sid.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {
    @Autowired
    private UtilisateurRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Utilisateur saveUser(Utilisateur u) {
        u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
        return userRepository.save(u);
    }
    @Override
    public Role saveRole(Role r) {
        return roleRepository.save(r);
    }
    @Override
    public Utilisateur findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public void addRoleToUser(String username, RoleEnum roleName) {
        Utilisateur user=userRepository.findByUsername(username);
        Role role=roleRepository.findByNomRole(roleName);
        user.getRoles().add(role);
    }
}
