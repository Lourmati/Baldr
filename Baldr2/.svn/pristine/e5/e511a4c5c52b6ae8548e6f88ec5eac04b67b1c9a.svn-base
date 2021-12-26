package ca.qc.bdeb.baldr.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * Méthodes utilitaires pour détecter des correspondances entre deux éléments,
 * ainsi que pour les manipuler par rapport à ces correspondances.
 *
 * @author Maxim Bernard
 */
public final class Correspondance {

    /**
     * Vérifie si une chaine correspond à un modèle constituté de : - points
     * d'interrogation (?) qui peuvent remplacer n'importe quel caractère; -
     * astérisques (*) qui peuvent remplacer n'importe quels caractères au moins
     * une fois.
     *
     * @param str Chaîne à tester.
     * @param pattern Le pattern avec lequel faire le test.
     * @return Vrai si la chaîne correspond, sinon faux.
     */
    public static boolean stringMatchesPattern(File str, String pattern) {
        WildcardFileFilter regex = new WildcardFileFilter(pattern);
        boolean regexValid = regex.accept(str);
        return regexValid;
    }

    private static boolean stringMatchesSansEtoiles(String str, String pattern) {
        char[] chaine = str.toCharArray();
        char[] patt = pattern.toCharArray();
        if (chaine.length != patt.length) {
            return false;
        }

        for (int i = 0; i < chaine.length; ++i) {
            if (patt[i] != '?' && chaine[i] != patt[i]) {
                return false;
            }
        }
        return true;
    }



    /**
     * Équivaut à {@link String#split(String)}, mais corrige des comportements
     * indésirables. TODO(pascal) : Identifier les comportements indésirables et
     * en faire la liste (dans l'éventualité où une nouvelle version de Java
     * venait à les corriger).
     *
     * @param str Chaine à séparer aux «*».
     * @return Tableau de sous-chaines.
     */
    private static String[] splitAroundWildcard(String str) {
        boolean chaineVideDebut = false, chaineVideFin = false;

        if (str.startsWith("*")) {
            chaineVideDebut = true;
            str = str.substring(1);
        }

        if (str.endsWith("*")) {
            chaineVideFin = true;
            str = str.substring(0, str.length() - 1);
        }

        ArrayList<String> resultat
                = new ArrayList<>(Arrays.asList(str.split("\\*")));

        if (chaineVideDebut) {
            resultat.add(0, "");
        }

        if (chaineVideFin) {
            resultat.add("");
        }

        return resultat.toArray(new String[0]);
    }

    /**
     * Équivaut à {@link String#indexOf(String)}, mais traite les «?» comme
     * s'ils pouvaient remplacer n'importe quel caractère.
     *
     * @param initStr Chaine complète.
     * @param queryStr Sous-chaine à rechercher.
     * @return Indice de [queryStr] dans [initStr].
     */
    public static int indexOfSaufCaracteresInconnus(
            String initStr, String queryStr) {
        char[] init = initStr.toCharArray();
        char[] query = queryStr.toCharArray();

        for (int index = 0; index < init.length; index++) {
            if (init[index] == query[0] || query[0] == '?') {
                int qindex = 1;

                while (qindex < query.length
                        && index + qindex < init.length
                        && (init[index + qindex] == query[qindex]
                        || query[qindex] == '?')) {
                    qindex++;
                }
                if (qindex == query.length) {
                    return index;
                }
            }
        }

        return -1;
    }

    /**
     * Retourne vrai si [str] est égale à [check]. La vérification exclut les
     * caractères «?» de [check], pour pouvoir correspondre à n'importe quel
     * caractère.
     *
     * @param str La chaine à tester.
     * @param check La chaine avec laquelle faire le test.
     * @return Si la chaine est égale au test, excluant les «?».
     */
    public static boolean correspondanceSaufCaracteresInconnus(
            String str, String check) {
        char[] strInit = str.toCharArray();
        char[] strCheck = check.toCharArray();

        if (strInit.length != strCheck.length) {
            return false;
        }

        for (int i = 0; i < strInit.length; i++) {
            if (strCheck[i] != '?' && strInit[i] != strCheck[i]) {
                return false;
            }
        }

        return true;
    }
}
