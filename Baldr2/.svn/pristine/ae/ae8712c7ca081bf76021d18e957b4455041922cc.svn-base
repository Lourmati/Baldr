package ca.qc.bdeb.baldr.noyau;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe qui contient les informations sur les filtres. Utilisée par la classe
 * FiltreFichier.java.
 *
 * @author Vincent Bissonnette
 */
public class GestionnaireFiltres extends ArrayList {

    private static ArrayList<String> filtresAjout = new ArrayList<>();
    private static ArrayList<String> filtresExclu = new ArrayList<>();
    private Noyau noyau;
    private GestionnairePreferences preferences;

    /**
     * Le constructeur de la classe, qui la lie au noyau.
     *
     * @param noyau Le noyau du programme.
     */
    public GestionnaireFiltres(Noyau noyau) {
        this.noyau = noyau;

        this.preferences = noyau.getPrefs();
    }

    public GestionnaireFiltres() {

    }

    /**
     * Lit la préférence de filtres.
     */
    public void obtenirFiltresAjout() {
        String[] tab = preferences.readPref("FAJOUT").split(" ");

        filtresAjout.clear();

        if (tab[0].equals("")) {
            filtresAjout.add("*.java");
            filtresAjout.add("*.cs");
            filtresAjout.add("*.py");
            filtresAjout.add("*.docx");
            filtresAjout.add("*.pdf");
        } else {
            filtresAjout.addAll(Arrays.asList(tab));
        }
    }

    public void obtenirFiltresExclu() {
        String[] tab = preferences.readPref("FEXCLU").split(" ");

        filtresExclu.clear();

        if (tab[0].equals("")) {
            filtresExclu.add("*.java");
            filtresExclu.add("*.cs");
            filtresExclu.add("*.py");
            filtresExclu.add("*.docx");
            filtresExclu.add("*.pdf");
        } else {
            filtresExclu.addAll(Arrays.asList(tab));
        }
    }

    /**
     * Enregistre les filtres dans les préférences.
     */
    public void enregistrerFiltresAjout() {
        StringBuilder chaine = new StringBuilder();

        for (String filtre : filtresAjout) {
            chaine.append(filtre);
            chaine.append(" ");
        }
        preferences.writePref("FAJOUT", chaine.toString());
    }

    public void enregistrerFiltresExclu() {
        StringBuilder chaine = new StringBuilder();

        for (String filtre : filtresExclu) {
            chaine.append(filtre);
            chaine.append(" ");
        }
        preferences.writePref("FEXCLU", chaine.toString());
    }

    /**
     * Ajoute un filtre à la liste.
     *
     * @param filtre Le filtre à ajouter.
     */
    public void ajouterFiltre(String filtre) {
        filtresAjout.add(filtre);
    }

    /**
     * Supprime un filtre de la liste.
     *
     * @param filtre Le filtre à suprimer.
     */
    public void suprimerFiltreAjout(String filtre) {
        filtresAjout.remove(filtre);
    }

    /**
     * Permet d'accéder aux filtres d'ajout actuels.
     *
     * @return Les filtres.
     */
    public String[] getFiltresAjout() {
        return filtresAjout.toArray(new String[0]);
    }

    /**
     * Ajoute un filtre d'exclusion à la liste.
     *
     * @param filtre Le filtre à ajouter.
     */
    public void exclureFiltre(String filtre) {
        filtresExclu.add(filtre);
    }

    /**
     * Supprime un filtre d'exclusion de la liste.
     *
     * @param filtre Le filtre à suprimer.
     */
    public void suprimerFiltreExclu(String filtre) {
        filtresExclu.remove(filtre);
    }

    /**
     * Permet d'accéder aux filtres d'exclusion actuels.
     *
     * @return Les filtres.
     */
    public String[] getFiltresExclu() {
        return filtresExclu.toArray(new String[0]);
    }
}
