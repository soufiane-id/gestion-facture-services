package ma.sid.services;

import ma.sid.entities.Personne;

import java.util.List;

public interface PersonneService {

    List<Personne> recupererClients();

    Personne ajouterClient(Personne personne);

    Personne ajouterFournisseur(Personne personne);

    void supprimerPersonne(Long idPersonne);

    List<Personne> recupererFournisseurs();
}
