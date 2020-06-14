package ma.sid.dto.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT) // Serializing Enums to JSON
public enum NoteClient {
    DANGEREUX("DANGEREUX", 1),
    MAUVAIS("MAUVAIS", 2),
    NORMAL("NORMAL", 3),
    NORMAL_AVEC_ALERTE("NORMAL + ALERTE COMM", 4),
    NORMAL_AVEC_URGENCE("NORMAL + URGENCE COMM", 5),
    BON_CLIENT("BON CLIENT", 6),
    BON_CLIENT_AVEC_URGENCE("BON CLIENT + URGENCE COMM", 7),
    TRES_BON_CLIENT("TRES BON CLIENT", 8),
    ELITE("ELITE", 9),
    INACTIF_SANS_ARRIERES("INACTIF SANS ARRIERES", 10),
    INACTIF_AVEC_ARRIERES("INACTFIS AVEC ARRIERES", 11);

    private String libelle;
    private int degre;

    NoteClient(String libelle, int degre) {
        this.libelle = libelle;
        this.degre = degre;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getDegre() {
        return degre;
    }

    public void setDegre(int degre) {
        this.degre = degre;
    }
}
