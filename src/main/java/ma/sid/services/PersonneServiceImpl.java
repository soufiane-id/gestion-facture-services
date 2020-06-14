package ma.sid.services;

import ma.sid.dao.PersonneRepository;
import ma.sid.entities.Personne;
import ma.sid.dto.enums.TypePersonne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonneServiceImpl implements PersonneService {

    @Autowired
    private PersonneRepository personneRepository;

    @Override
    public List<Personne> recupererClients() {
        return personneRepository.findAllClients();
    }

    @Override
    public Personne ajouterClient(Personne personne){
            personne.setTypePersonne(TypePersonne.CLIENT);
            return personneRepository.save(personne);
    }

    @Override
    public Personne ajouterFournisseur(Personne personne) {
        personne.setTypePersonne(TypePersonne.FOURNISSEUR);
        return personneRepository.save(personne);
    }

    @Override
    public void supprimerPersonne(Long idPersonne) {
        personneRepository.deleteById(idPersonne);
    }

    @Override
    public List<Personne> recupererFournisseurs() {
        return personneRepository.findAllFournisseurs();
    }
}
