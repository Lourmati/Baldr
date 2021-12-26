package ca.qc.bdeb.baldr.noyau;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Gère les préférences.
 *
 * @author mpauze
 */
public class GestionnairePreferences {

    private Preferences prefs;

    /**
     * Le constructeur de la classe, qui associe le gestionnaire à une classe.
     *
     * @param c La classe avec laquelle s'associer.
     */
    public GestionnairePreferences(Class<?> c) {
        prefs = Preferences.userNodeForPackage(c);
    }

    /**
     * Lit une préférence.
     *
     * @param name Le nom de la préférence.
     * @param defaut Valeur à retourner lorsqu'impossible de lire la préférence.
     * @return Valeur précédemment associée avec
     * {@link #writePref(String, Object)}.
     */
    public Object readPref(String name, Object defaut) {
        if (defaut instanceof Boolean) {
            return prefs.getBoolean(name, (Boolean) defaut);
        }
        if (defaut instanceof Integer) {
            return prefs.getInt(name, (Integer) defaut);
        }
        if (defaut instanceof Double) {
            return prefs.getDouble(name, (Double) defaut);
        }
        if (defaut instanceof Float) {
            return prefs.getFloat(name, (Float) defaut);
        }
        if (defaut instanceof String) {
            return prefs.get(name, (String) defaut);
        }
        return null;
    }

    /**
     * Lit une préférence, sans valeur par défaut.
     *
     * @param name Le nom de la préférence.
     * @return Valeur, ou une chaîne vide.
     */
    public String readPref(String name) {
        return prefs.get(name, "");
    }

    /**
     * Écrit une préférence.
     *
     * @param name Le nom de la préférence.
     * @param valeur Valeur de la préférence.
     */
    public void writePref(String name, Object valeur) {
        if (valeur instanceof Boolean) {
            prefs.putBoolean(name, (Boolean) valeur);
        } else if (valeur instanceof Integer) {
            prefs.putInt(name, (Integer) valeur);            
        } else if (valeur instanceof Double) {
            prefs.putDouble(name, (Double) valeur);            
        } else if (valeur instanceof Float) {
            prefs.putFloat(name, (Float) valeur);
        } else if (valeur instanceof String) {
            prefs.put(name, (String) valeur);
        }
    }

    /**
     * Détermine si une préférence donnée existe déjà.
     *
     * @param name Le nom de la préférence.
     * @return Vrai si la préférence donnée existe, sinon faux.
     */
    public boolean prefExists(String name) {
        String[] list = null;

        try {
            list = prefs.keys();
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
            return false;
        }

        for (String str : list) {
            if (str.equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Écrit de façon permanente les préférences sur le disque.
     */
    public void flushPrefs() {
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            ex.printStackTrace();
        }
    }

}
