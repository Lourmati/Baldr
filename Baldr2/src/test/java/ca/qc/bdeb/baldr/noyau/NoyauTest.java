package ca.qc.bdeb.baldr.noyau;

import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Usager
 */
public class NoyauTest {

    Noyau noyauTest;
    GestionnairePreferences preferences;

    public NoyauTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        noyauTest = new Noyau();
        preferences = noyauTest.getPrefs();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReadWritePref() throws URISyntaxException {
        preferences.writePref("TEST", false);
        assertEquals(false, (Boolean) preferences.readPref("TEST", true));

        preferences.writePref("TEST", 1);
        assertEquals(1, (int) preferences.readPref("TEST", 0));

        preferences.writePref("TEST", 1.0);
        assertEquals(1.0, (double) preferences.readPref("TEST", 0.0), 0);

        preferences.writePref("TEST", 1f);
        assertEquals(1f, (float) preferences.readPref("TEST", 0f), 0);

        preferences.writePref("TEST", "test");
        assertEquals("test", (String) preferences.readPref("TEST", ""));

        preferences.writePref("TEST", 'c');
        assertNull(preferences.readPref("TEST", 'q'));

        preferences.writePref("TEST", "banana");
        assertEquals("banana", preferences.readPref("TEST"));
    }

    @Test
    public void testFlushPrefs() throws URISyntaxException {
        boolean prefApres;

        preferences.writePref("TEST", false);

        preferences.flushPrefs();
        Noyau noyauTestPrefsApres = new Noyau();
        prefApres = (Boolean) noyauTestPrefsApres.getPrefs().readPref("TEST", true);
        assertEquals((Boolean) preferences.readPref("TEST", true), prefApres);
    }

    @Test
    public void testPrefExists() throws URISyntaxException {
        preferences.writePref("PREVIEW", "");
        assertTrue(preferences.prefExists("PREVIEW"));
        assertFalse(preferences.prefExists("ALLO"));
    }

    @Test
    public void testGetNoyauNotNull() {

        assertNotNull(noyauTest.getProjetCourant());
    }
}
