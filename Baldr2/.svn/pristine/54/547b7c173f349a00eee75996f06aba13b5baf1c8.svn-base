package ca.qc.bdeb.baldr.ihm;

import ca.qc.bdeb.baldr.noyau.GestionnairePreferences;
import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 *
 * @author Arnaud Brouillet-Gag
 */
public class GestionnaireI18N extends Observable {
    
    private ResourceBundle messages;
    private GestionnairePreferences prefs;
    
    public GestionnaireI18N(GestionnairePreferences prefs){
        this.prefs = prefs;
    }
    
    public void rechargerLangue(){
        String loc = prefs.readPref("LOCALE");

        if (loc != null && loc.length() > 1) {
            Locale.setDefault(new Locale(loc));
        }

        messages = ResourceBundle.getBundle("i18n/Baldr");
        setChanged();
        notifyObservers(messages);
    }
    
}
