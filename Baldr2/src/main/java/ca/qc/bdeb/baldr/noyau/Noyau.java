/*
 * Noyau.java
 *
 * Created on 14 avril 2007, 17:35
 *$Id: Noyau.java 211 2007-05-27 13:05:43Z zeta $
 */
package ca.qc.bdeb.baldr.noyau;

/**
 * Crée et gère les projets. Se fie à la classe GestionnairePreferences pour
 * gérer les préférences.
 *
 * @author zeta
 * @author mbernard
 */
public class Noyau {

    private Projet projetCourant;

    private GestionnairePreferences prefs;

    /**
     * Crée un nouveau projet par défaut.
     */
    public Noyau() {
        prefs = new GestionnairePreferences(this.getClass());
        projetCourant = new Projet(this);
    }
    
    public GestionnairePreferences getPrefs() {
        return prefs;
    }
    
    public Projet getProjetCourant() {
        return projetCourant;
    }
}
