package ca.qc.bdeb.baldr.utils;

import java.util.ArrayList;

/**
 * Méthodes utilitaires pour travailler sur les tableaux.
 *
 * @author Maxim Bernard
 */
public final class ArrayUtil {

    /**
     * Transforme une liste d'entiers en tableau d'entiers.
     *
     * @param liste Une liste d'entiers.
     * @return Un tableau d'entiers.
     */
    public static int[] toIntArray(ArrayList<Integer> liste) {
        int[] tabInt = new int[liste.size()];
        for (int i = 0; i < tabInt.length; i++) {
            tabInt[i] = liste.get(i);
        }
        return tabInt;
    }

    /**
     * Ajoute un élément au début d'un tableau de chaines de caractères.
     *
     * @param init Le tableau d'origine.
     * @param elm L'élément à ajouter.
     * @return Le nouveau tableau.
     */
    public static String[] prependString(String[] init, String elm) {
        String[] nouv = new String[init.length + 1];
        nouv[0] = elm;

        for (int i = 1; i < nouv.length; i++) {
            nouv[i] = init[i - 1];
        }

        return nouv;
    }

    /**
     * Copie un certain nombre d'éléments d'un tableau dans un autre, à partir
     * d'un index fourni.
     *
     * @param <T> Type des éléments des tableaux.
     * @param to Tableau où copier les valeurs.
     * @param toStartIndex L'indice de départ pour la destination.
     * @param from Tableau d'où tirer les valeurs à copier.
     * @param fromStartIndex L'indice de départ de l'origine.
     * @param count Le nombre d'éléments à copier.
     */
    public static <T> void copyRangeInto(T[] to, int toStartIndex,
            T[] from, int fromStartIndex, int count) {
        for (int i = 0; i < count; i++) {
            to[toStartIndex + i] = from[fromStartIndex + i];
        }
    }

    /**
     * Enlève les blancs avant et après chaque chaîne d'un tableau de chaines,
     * puis retire du tableau les chaînes vides.
     *
     * @param init Tableau de chaînes à nettoyer.
     * @return Le tableau nettoyé.
     */
    public static String[] trimStringsInArray(String[] init) {
        String[] newArr = init.clone();

        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = newArr[i].trim();
        }

        ArrayList<String> mcxNonVides = new ArrayList<>();

        for (String str : newArr) {
            if (!str.isEmpty()) {
                mcxNonVides.add(str);
            }
        }

        return mcxNonVides.toArray(new String[0]);
    }
}
