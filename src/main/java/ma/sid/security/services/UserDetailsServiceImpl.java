package ma.sid.security.services;

import ma.sid.entities.Utilisateur;
import ma.sid.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UtilisateurService utilisateurService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utilisateur user = utilisateurService.findUserByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException(username);
		/*Collection<GrantedAuthority> authorities=new ArrayList<>();
		user.getRoles().forEach(r->{
			authorities.add(new SimpleGrantedAuthority(r.getNomRole().name()));
		});*/
		return UserDetailsImpl.build(user);
		//return new UserResponse(user.getUsername(), user.getPassword(), user.getNom(), user.getPrenom(), authorities);
	}

}
