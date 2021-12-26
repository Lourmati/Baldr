/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.baldr.noyau;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Vincent
 */
public class FiltreTest {

    GestionnaireFiltres gestFiltre;
    Noyau noyau;
    static GestionnairePreferences preferences;
    static String[] tabPref;

    public FiltreTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        //tabPref = preferences.readPref("FILTER").split(" ");
    }

    @AfterClass
    public static void tearDownClass() {
        //for (String pref : tabPref)
            //preferences.writePref("FILTER", pref);
    }

    @Before
    public void setUp() {
        noyau = new Noyau();
        gestFiltre = new GestionnaireFiltres(noyau);
    }

    @After
    public void tearDown() {
        gestFiltre = null;
        noyau = null;
    }

    /**
     * Test of obtenirFiltres method, of class GestionnaireFiltres.
     */
    @Test
    public void testObtenirFiltreSansPref() {
        noyau.getPrefs().writePref("FAJOUT", "");
        gestFiltre.obtenirFiltresAjout();
        String[] filtres = gestFiltre.getFiltresAjout();
        assertEquals(filtres.length, 5);
        assertEquals(filtres[0], "*.java");
        assertEquals(filtres[1], "*.cs");
        assertEquals(filtres[2], "*.py");
        assertEquals(filtres[3], "*.docx");
        assertEquals(filtres[4], "*.pdf");
    }

    @Test
    public void testObtenirFiltreAvecPref() {
        noyau.getPrefs().writePref("FAJOUT", "*.java *.py");
        gestFiltre.obtenirFiltresAjout();
        String[] filtres = gestFiltre.getFiltresAjout();
        assertEquals(2, filtres.length);
        assertEquals(filtres[0], "*.java");
        assertEquals(filtres[1], "*.py");
    }

    /**
     * Test of enregistrerFiltres method, of class GestionnaireFiltres.
     */
    @Test
    public void testEnregistrerFiltre() {
        noyau.getPrefs().writePref("FAJOUT", "*.java *.cs");
        gestFiltre.obtenirFiltresAjout();
        gestFiltre.enregistrerFiltresAjout();
        String[] filtres = gestFiltre.getFiltresAjout();
        assertEquals(2, filtres.length);
        assertEquals(filtres[0], "*.java");
        assertEquals(filtres[1], "*.cs");
    }

    /**
     * Test of ajouter method, of class GestionnaireFiltres.
     */
    @Test
    public void testAjouter() {
        noyau.getPrefs().writePref("FAJOUT", "");
        gestFiltre.ajouterFiltre("*.java");
        gestFiltre.ajouterFiltre("*.cs");
        String[] filtres = gestFiltre.getFiltresAjout();
        assertEquals(2, filtres.length);
        assertEquals(filtres[0], "*.java");
        assertEquals(filtres[1], "*.cs");
    }

    /**
     * Test of suprimer method, of class GestionnaireFiltres.
     */
    @Test
    public void testSuprimer() {
        noyau.getPrefs().writePref("FAJOUT", "*.java *.cs");
        gestFiltre.obtenirFiltresAjout();
        String[] filtres = gestFiltre.getFiltresAjout();
        assertEquals(2, filtres.length);
        assertEquals(filtres[0], "*.java");
        assertEquals(filtres[1], "*.cs");
        gestFiltre.suprimerFiltreAjout("*.cs");
        filtres = gestFiltre.getFiltresAjout();
        assertEquals(1, filtres.length);
        assertEquals(filtres[0], "*.java");
    }

}
