package ca.qc.bdeb.baldr.formattage;

import ca.qc.bdeb.baldr.utils.Extension;
import java.io.File;
import java.util.LinkedList;
import static ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author plaprade
 */
public class CommentParser {

    public enum EstCommentaire {

        OUI,
        PREMIER_CARACTERE,
        NON
    }

    char caractere;
    char caracterePrecedent;
    char marqueurChaine;

    boolean uneLigne = false;
    boolean dansCommentaire = false;
    boolean dansChaineCaractere = false;

    boolean estPremierCaractereCommentaire = false;
    boolean estPasCaractereCommentaire = false;

    TypeCommentaire typeCommentaire;
    ArrayList<Character> characteresFichier = new ArrayList();

    LinkedList<Character> etudiesUneLigne = new LinkedList();
    LinkedList<Character> etudiesMultiDebut = new LinkedList();
    LinkedList<Character> etudiesMultiFin = new LinkedList();

    LinkedList<Boolean> niveauCommentaire = new LinkedList();
    List<Character> caracteresIgnores = new ArrayList();

    public CommentParser(File fichier) {
        typeCommentaire = TypeCommentaire.determinerCommentaires(Extension.getExtension(fichier));
    }

    /**
     * Invoqué sur chaque caractere du fichier
     *
     * @param car
     */
    public void lireCaractere(char car) {

        caracterePrecedent = caractere;
        caractere = car;

        determinerPosition();

        if (!dansChaineCaractere && estDebutCommentaire()) {
            dansCommentaire = true;
            niveauCommentaire.push(uneLigne);
        }
    }

    private void determinerPosition() {
        if (dansCommentaire) {
            dansChaineCaractere = false;
        } else if (dansChaineCaractere) {
            dansChaineCaractere = !estMarqueurFinChaine();
        } else {
            dansChaineCaractere = estMarqueurDebutChaine();
        }
    }

    private boolean estMarqueurDebutChaine() {
        boolean estMarqueur = ((caractere == '\'' || caractere == '"') && caracterePrecedent != '\\');
        if (estMarqueur) {
            marqueurChaine = caractere;
        }
        return estMarqueur;
    }
    
    private boolean estMarqueurFinChaine() {
        return caractere == marqueurChaine && caracterePrecedent != '\\';
    }

    private boolean estDebutCommentaire() {
        if (typeCommentaire == null) {
            return false;
        }
        
        etudiesUneLigne.add(caractere);
        etudiesMultiDebut.add(caractere);
        EstCommentaire estCommentaire = typeCommentaire.estCommentaire(etudiesUneLigne, etudiesMultiDebut);
        changerStatusLigne();

        if (estCommentaire == OUI) {
            estPremierCaractereCommentaire = false;
            estPasCaractereCommentaire = false;
            caracteresIgnores.clear();
            return true;
        } else if (estCommentaire == PREMIER_CARACTERE) {
            estPremierCaractereCommentaire = true;
            estPasCaractereCommentaire = false;
            if (niveauCommentaire.isEmpty()) {
                caracteresIgnores.add(caractere);
            }
        } else if (estCommentaire == NON && estPremierCaractereCommentaire) {
            estPremierCaractereCommentaire = false;
            estPasCaractereCommentaire = true;
            if (!dansCommentaire) {
                caracteresIgnores.add(caractere);
            }
        }
        return false;
    }

    public void changerStatusLigne() {
        if (typeCommentaire.getEstMultiLignes() == OUI) {
            uneLigne = false;
        } else if (typeCommentaire.getEstUneLigne() == OUI) {
            uneLigne = true;
        }
        if (typeCommentaire.getEstUneLigne() == NON) {
            etudiesUneLigne.clear();
        }
    }

    public String retournerCaractereChaine() {
        String retour = "";
        
        if (dansChaineCaractere) {
            retour = caractere + "";
        } else if (dansCommentaire) {
            if (estFinCommentaire()) {
                retour = retournerCaractereFin();
            }
        } else if (estPremierCaractereCommentaire) {
            retour = "";
        } else if (estPasCaractereCommentaire && !caracteresIgnores.isEmpty()) {
            estPasCaractereCommentaire = false;

            if (!dansCommentaire) {

                for (char car : caracteresIgnores) {
                    retour += car;
                }
                caracteresIgnores.clear();
            }
        } else {
            retour = caractere + "";
        }

        retour = retour.replaceAll("\\s+","");
        return retour;
    }

    private boolean estFinCommentaire() {
        if (uneLigne && caractere == '\n') {
            return true;
        }
        return typeCommentaire.estCommentaireMultiLignesFin(etudiesMultiFin, caractere);
    }
    
    private String retournerCaractereFin() {
        uneLigne = niveauCommentaire.pop();
        dansCommentaire = false;

        if (niveauCommentaire.isEmpty()) {
            if (uneLigne) {
                return caractere + "";
            }
        } else {
            uneLigne = niveauCommentaire.getLast();
        }
        return "";
    }
}
