package ma.sid.services;

import ma.sid.dao.EcranAutoriseRepository;
import ma.sid.dao.RoleRepository;
import ma.sid.dao.UtilisateurRepository;
import ma.sid.entities.EcranAutorise;
import ma.sid.entities.Role;
import ma.sid.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UtilisateurRoleServiceImpl implements UtilisateurRoleService {
    @Autowired
    private UtilisateurRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EcranAutoriseRepository ecranAutoriseRepository;
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
    public void addRoleToUser(String username, String roleName) {
        Utilisateur user = userRepository.findByUsername(username);
        Role role = roleRepository.findByNomRole(roleName);
        user.setRoles(Arrays.asList(role));
    }

    @Override
    public List<Utilisateur> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Utilisateur updateUser(Utilisateur utilisateur) {
        if(StringUtils.isEmpty(utilisateur.getPassword())) {
            //Cas de modification: on valorise le mdp avec lancien.
            Utilisateur oldUser = userRepository.getOne(utilisateur.getId()); // getOne = Lazy Loading => we add @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) into Utilisateur class
            utilisateur.setPassword(oldUser.getPassword());
        }
        userRepository.save(utilisateur);
        addRoleToUser(utilisateur.getUsername(), utilisateur.getRoles().iterator().next().getNomRole());
        return utilisateur;
    }

    @Override
    public List<String> getEcransByRoleName(Long roleId) {
        List<String> result = new ArrayList<>();
        roleRepository.findById(roleId).get().getEcranAutorise()
                .stream()
                .map(ecran -> ecran.getNomEcran())
                .forEach(r -> result.add(r));
        return result;
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepository.findByNomRole(roleName);
    }

    @Override
    public Role addEcransToRole(Long roleId, Set<String> ecrans) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.ifPresent(r ->
                {
                    r.addEcransAutorises(convertStringToEcranAutorise(ecrans));
                });
        return role.orElse(null);
    }

    @Override
    public Role addRole(String nomRole) {
        Role role = new Role();
        role.setNomRole(nomRole);
        return roleRepository.save(role);
    }

    private Set<EcranAutorise> convertStringToEcranAutorise(Set<String> ecrans) {
        Set<EcranAutorise> result = new HashSet<>();
        EcranAutorise ecranAutorise;
        for (String ecr: ecrans) {
            ecranAutorise = ecranAutoriseRepository.findByNomEcran(ecr);
            result.add(ecranAutorise);
        }
        return result;
    }
}
