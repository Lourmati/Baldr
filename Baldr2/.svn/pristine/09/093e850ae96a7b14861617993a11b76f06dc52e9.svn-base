package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author adore
 */
public final class Tri {

    public static enum Type {

        
        ResultatCroissant("result_increasing"),
        AlphabetiqueCroissant("alphabetical_increasing"),
        AlphabetiqueDecroissant("alphabetical_decreasing"),
        ExtensionCroissant("extension_increasing"),
        ExtensionDecroissant("extension_decreasing"),
        ResultatDecroissant("result_decreasing"),
        MoyenneCroissant("average_increasing"),
        MoyenneDecroissant("average_decreasing");

        private final String label;

        private Type(String s) {
            this.label = s;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public static class TypeI18N {

        private Type type;
        private ResourceBundle messages;

        public TypeI18N(Type type, ResourceBundle messages) {
            this.type = type;
            this.messages = messages;
        }

        /*
        Méthode modifié en enlevant le IF car condition s'applique toujours
        dans le contexte actuel du programme qui ne permet pas a
        l'utilisateur de changer les ressources manuellement.
         */
        @Override
        public String toString() {
            return messages.getString(type.toString());
        }

        public Type getType() {
            return type;
        }

    }

    public static void trier(List<File> fichiers, MatriceTriangulaire resultats, Type type) {
        if (fichiers == null) {
            return;
        }

        switch (type) {
            case ResultatCroissant:
                triResultatCroissant(fichiers, resultats);
                break;
            case AlphabetiqueCroissant:
                triAlphabetiqueCroissant(fichiers, resultats);
                break;
            case AlphabetiqueDecroissant:
                triAlphabetiqueDecroissant(fichiers, resultats);
                break;
            case ExtensionCroissant:
                triExtensionCroissant(fichiers, resultats);
                break;
            case ExtensionDecroissant:
                triExtensionDecroissant(fichiers, resultats);
                break;
            case ResultatDecroissant:
                triResultatDecroissant(fichiers, resultats);
                break;
            case MoyenneCroissant:
                double[] moyCroiss = triMoyenneCroissant(fichiers, resultats);
                break;
            case MoyenneDecroissant:
                double[] moyDecroiss = triMoyenneDecroissant(fichiers, resultats);
                break;
        }
    }

    private static void echangerFichiers(List<File> fichs, int i, int j, MatriceTriangulaire resultats) {
        File temporaire = fichs.get(j);
        fichs.set(j, fichs.get(i));
        fichs.set(i, temporaire);
        resultats.echangerValeurs(i, j);
    }

    public static void triAlphabetiqueCroissant(List<File> fichs, MatriceTriangulaire resultats) {
        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {
                int longueur = fichs.get(i).getName().length();
                if (longueur > fichs.get(j).getName().length()) {
                    longueur = fichs.get(j).getName().length();
                }
                for (int k = 0; k < longueur; k++) {
                    if (fichs.get(i).getName().charAt(k) > fichs.get(j).getName().charAt(k)) {
                        echangerFichiers(fichs, i, j, resultats);
                        break;
                    } else if (fichs.get(i).getName().charAt(k) < fichs.get(j).getName().charAt(k)) {
                        break;
                    } else if (k == longueur - 1 && fichs.get(i).getName().length() > fichs.get(j).getName().length()) {
                        echangerFichiers(fichs, i, j, resultats);
                    }
                }
            }
        }
    }

    public static void triAlphabetiqueDecroissant(List<File> fichs, MatriceTriangulaire resultats) {
        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {
                int longueur = fichs.get(i).getName().length();
                if (longueur > fichs.get(j).getName().length()) {
                    longueur = fichs.get(j).getName().length();
                }
                for (int k = 0; k < longueur; k++) {

                    if (fichs.get(i).getName().charAt(k) < fichs.get(j).getName().charAt(k)) {
                        echangerFichiers(fichs, i, j, resultats);
                        break;
                    } else if (fichs.get(i).getName().charAt(k) > fichs.get(j).getName().charAt(k)) {
                        break;
                    } else if (k == longueur - 1 && fichs.get(i).getName().length() < fichs.get(j).getName().length()) {
                        echangerFichiers(fichs, i, j, resultats);
                    }
                }
            }
        }
    }

    public static void triExtensionDecroissant(List<File> fichs, MatriceTriangulaire resultats) {
        triAlphabetiqueDecroissant(fichs, resultats);

        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {

                String fichsI = fichs.get(i).getName().substring(fichs.get(i).getName().lastIndexOf('.'));
                String fichsJ = fichs.get(j).getName().substring(fichs.get(j).getName().lastIndexOf('.'));
                int longueur = fichsI.length();
                if (longueur > fichsJ.length()) {
                    longueur = fichsJ.length();
                }
                for (int k = 0; k < longueur; k++) {

                    if (fichsI.charAt(k) < fichsJ.charAt(k)) {
                        echangerFichiers(fichs, i, j, resultats);
                        break;
                    } else if (fichsI.charAt(k) > fichsJ.charAt(k)) {
                        break;
                    } else if (k == longueur - 1 && fichsI.length() < fichsJ.length()) {
                        echangerFichiers(fichs, i, j, resultats);
                    }

                }
            }
        }
    }

    public static void triExtensionCroissant(List<File> fichs, MatriceTriangulaire resultats) {
        triAlphabetiqueCroissant(fichs, resultats);

        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {

                String fichsI = fichs.get(i).getName().substring(fichs.get(i).getName().lastIndexOf('.'));
                String fichsJ = fichs.get(j).getName().substring(fichs.get(j).getName().lastIndexOf('.'));
                int longueur = fichsI.length();
                if (longueur > fichsJ.length()) {
                    longueur = fichsJ.length();
                }
                for (int k = 0; k < longueur; k++) {
                    if (fichsI.charAt(k) > fichsJ.charAt(k)) {
                        echangerFichiers(fichs, i, j, resultats);
                        break;
                    } else if (fichsI.charAt(k) < fichsJ.charAt(k)) {
                        break;
                    } else if (k == longueur - 1 && fichsI.length() > fichsJ.length()) {
                        echangerFichiers(fichs, i, j, resultats);
                    }

                }
            }
        }
    }

    public static void triResultatCroissant(List<File> fichs, MatriceTriangulaire resultats) {

        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {
                if (resultats.getMinRes(i) > resultats.getMinRes(j)) {
                    echangerFichiers(fichs, i, j, resultats);
                }
            }
        }
    }

    public static void triResultatDecroissant(List<File> fichs, MatriceTriangulaire resultats) {
        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {
                if (resultats.getMinRes(i) < resultats.getMinRes(j)) {
                    echangerFichiers(fichs, i, j, resultats);
                }
            }
        }
    }

    public static double[] triMoyenneCroissant(List<File> fichs, MatriceTriangulaire resultats) {

        double[] moyenneLigne = new double[fichs.size()];

        for (int i = 0; i < fichs.size(); i++) {
            for (int j = 0; j < fichs.size(); j++) {
                if (i != j) {
                    moyenneLigne[i] += resultats.getRes(i, j);
                }
            }
            moyenneLigne[i] /= fichs.size();
        }

        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {
                if (moyenneLigne[i] > moyenneLigne[j]) {
                    echangerFichiers(fichs, i, j, resultats);
                    double temp = moyenneLigne[j];
                    moyenneLigne[j] = moyenneLigne[i];
                    moyenneLigne[i] = temp;
                }
            }
        }
        
        return moyenneLigne;
    }

    public static double[] triMoyenneDecroissant(List<File> fichs, MatriceTriangulaire resultats) {

        double[] moyenneLigne = new double[fichs.size()];

        for (int i = 0; i < fichs.size(); i++) {
            for (int j = 0; j < fichs.size(); j++) {
                if (i != j) {
                    moyenneLigne[i] += resultats.getRes(i, j);
                }
            }
            moyenneLigne[i] /= fichs.size();
        }

        for (int i = 0; i < fichs.size(); i++) {
            for (int j = i + 1; j < fichs.size(); j++) {
                if (moyenneLigne[i] < moyenneLigne[j]) {
                    echangerFichiers(fichs, i, j, resultats);
                    double temp = moyenneLigne[j];
                    moyenneLigne[j] = moyenneLigne[i];
                    moyenneLigne[i] = temp;
                }
            }
        }
        
        return moyenneLigne;
    }
}
