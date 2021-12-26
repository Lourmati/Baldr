package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author adore
 */
public class TriTest {

    ArrayList<File> fichiers;
    Task analys;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            fichiers = FaireTableauFile();
        } catch (URISyntaxException ex) {
            Logger.getLogger(TriTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        analys = new Task();
        analys.setConcatenation(false);

        analys.setFichiers(fichiers);
        analys.lancerAnalyse();
    }

    @After
    public void tearDown() {
    }

    public ArrayList<File> FaireTableauFile() throws URISyntaxException {
        ArrayList<URL> url = new ArrayList();
        url.add(getClass().getResource("/LoremIpsum1.txt"));
        url.add(getClass().getResource("/LoremIpsum1.txt"));
        url.add(getClass().getResource("/LoremIpsum2.txt"));
        url.add(getClass().getResource("/test.txt"));
        url.add(getClass().getResource("/LoremIpsum3.txt"));
        url.add(getClass().getResource("/TestTri.bidonx"));
        url.add(getClass().getResource("/TestTri.bidon"));
        url.add(getClass().getResource("/TestTri1.allo"));

        ArrayList<File> fichs = new ArrayList();

        for (URL lien : url) {
            fichs.add(new File(lien.toURI()));
        }

        return fichs;
    }

    @Test // Test qui vérifie si l'objet existe
    public void testCreationObjet() {
        Tri T = new Tri();
        boolean isObject = (T != null);
        // System.out.println(isObject); // Sort False
        assertTrue(isObject);
    }

    @Test
    public void testTypeI18NConstructeur() {
        Tri.Type[] types = Tri.Type.values(); // Copie de ENUM dans Tableau
        ResourceBundle messages = ResourceBundle.getBundle("i18n/Baldr");

        Tri.TypeI18N[] tabTypesExact = new Tri.TypeI18N[types.length];

        // Remplissement du tabTypesExact avec tous les types et messages
        for (int i = 0; i < tabTypesExact.length; i++) {
            tabTypesExact[i] = new Tri.TypeI18N(types[i], messages);
        }

        /*
        Test qui vérifie la correspondance des termes de l'énumeration
        DIRECTEMENT par valeur de type sans faire appel a toString()
         */
        int i = 0;
        for (Tri.TypeI18N T : tabTypesExact) {
            assertEquals(T, tabTypesExact[i]);
            i++;
        }
    }

    @Test
    public void testTypeI18NType() {
        Tri.Type[] types = Tri.Type.values(); // Copie de ENUM dans Tableau
        ResourceBundle messages = ResourceBundle.getBundle("i18n/Baldr");

        Tri.TypeI18N[] tabTypesExact = new Tri.TypeI18N[types.length];

        // Remplissement du tabTypesExact avec tous les types et messages
        for (int i = 0; i < tabTypesExact.length; i++) {
            tabTypesExact[i] = new Tri.TypeI18N(types[i], messages);
        }

        /*
        Test qui vérifie avec appel du getType
         */
        int i = 0;
        for (Tri.TypeI18N T : tabTypesExact) {
            assertEquals(T.getType(), tabTypesExact[i].getType());
            i++;
        }
    }

    @Test
    public void testTypeI118NString() {
        Tri.Type[] types = Tri.Type.values(); // Copie de ENUM dans Tableau
        ResourceBundle messages = ResourceBundle.getBundle("i18n/Baldr");

        Tri.TypeI18N[] tabTypesExact = new Tri.TypeI18N[types.length];

        // Remplissement du tabTypesExact avec tous les types et messages
        for (int i = 0; i < tabTypesExact.length; i++) {
            tabTypesExact[i] = new Tri.TypeI18N(types[i], messages);
        }

        /*
        Test qui vérifie avec appel du toString()
         */
        int i = 0;
        for (Tri.TypeI18N T : tabTypesExact) {
            assertEquals(T.toString(), tabTypesExact[i].toString());
            i++;
        }
    }

    @Test
    public void TriNomTest() {
        assertEquals("alphabetical_increasing",
                Tri.Type.AlphabetiqueCroissant.toString());
    }

    @Test
    public void TriAlphabetiqueCroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.AlphabetiqueCroissant);

        // FichiersResultats.size donne REAL et non INDEXED
        for (int i = 0; i < fichiersResultats.size() - 1; i++) {

            String fichier1 = fichiersResultats.get(i).getName();
            String fichier2 = fichiersResultats.get(i + 1).getName();

            int indiceLex = fichier1.compareTo(fichier2);

            assertTrue(indiceLex <= 0); // Négatif pour croissant
        }
    }

    @Test
    public void TriAlphabetiqueDecroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.AlphabetiqueDecroissant);

        for (int i = 0; i < fichiersResultats.size() - 1; i++) {

            String fichier1 = fichiersResultats.get(i).getName();
            String fichier2 = fichiersResultats.get(i + 1).getName();

            int indiceLex = fichier1.compareTo(fichier2);

            assertTrue(indiceLex >= 0); // Positif pour décroissant
        }

    }

    @Test
    public void TriExtensionCroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.ExtensionCroissant);

        for (int i = 0; i < fichiersResultats.size() - 1; i++) {

            int posExt1 = fichiersResultats.get(i).getName().indexOf('.');
            int posExt2 = fichiersResultats.get(i + 1).getName().indexOf('.');

            String extFich1 = fichiersResultats.get(i).getName().
                    substring(posExt1);
            String extFich2 = fichiersResultats.get(i + 1).getName().
                    substring(posExt2);

            int indiceLex = extFich1.compareTo(extFich2);

            assertTrue(indiceLex <= 0); // Extension Croissante
        }
      
    }  
    
    @Test
    public void TriExtensionDecroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.ExtensionDecroissant);

        for (int i = 0; i < fichiersResultats.size() - 1; i++) {

            int posExt1 = fichiersResultats.get(i).getName().indexOf('.');
            int posExt2 = fichiersResultats.get(i + 1).getName().indexOf('.');

            String extFich1 = fichiersResultats.get(i).getName().
                    substring(posExt1);
            String extFich2 = fichiersResultats.get(i + 1).getName().
                    substring(posExt2);

            int indiceLex = extFich1.compareTo(extFich2);

            assertTrue(indiceLex >= 0); // Extension Décroissante
        }
    }

    @Test
    public void TriResultatCroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.ResultatCroissant);

        // Test la valeur minimale de la ligne
        for (int i = 0; i < resultats.getLength() - 1; i++) {

            float minLigne1 = resultats.getMinRes(i);
            float minLigne2 = resultats.getMinRes(i + 1);

            assertTrue(minLigne1 <= minLigne2);
        }
    }

    @Test
    public void TriNull() {
        Tri.trier(null, null, Tri.Type.ResultatCroissant);
        assertTrue(true); // Si le test ne lance pas un exception
    }

    @Test
    public void TriResultatDecroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.ResultatDecroissant);

        // Test la valeur minimale de la ligne
        for (int i = 0; i < resultats.getLength() - 1; i++) {

            float minLigne1 = resultats.getMinRes(i);
            float minLigne2 = resultats.getMinRes(i + 1);

            assertTrue(minLigne1 >= minLigne2);
        }
    }

    @Test
    public void TriMoyenneCroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.MoyenneCroissant);

        double[] moyennes = Tri.triMoyenneCroissant(fichiersResultats,
                resultats);

        for (int i = 0; i < moyennes.length - 1; i++) {

            double moy1 = moyennes[i];
            double moy2 = moyennes[i + 1];

            assertTrue(moy1 <= moy2);
        }
    }

    @Test
    public void TriMoyenneDecroissantTest() {
        MatriceTriangulaire resultats = analys.getResults();
        List<File> fichiersResultats = analys.getFichiersResultats();
        Tri.trier(fichiersResultats, resultats, Tri.Type.MoyenneDecroissant);

        double[] moyennes = Tri.triMoyenneDecroissant(fichiersResultats,
                resultats);

        for (int i = 0; i < moyennes.length - 1; i++) {

            double moy1 = moyennes[i];
            double moy2 = moyennes[i + 1];

            assertTrue(moy1 >= moy2);
        }
    }

    /**
     * Ce test a été créé pour obtenir des résultats non seulement centrés sur
     * des résultats corrects, mais également des résultats attendus. Il analyse
     * quatre fichiers qui font toutes la même chose et s'attend à un ordre de
     * résultats prévisible selon la similarité du code, comme on cherche à
     * utiliser ce logiciel pour prévenir la fraude. Il ne teste donc pas
     * directement une méthode spécifique.
     */
    @Test
    public void TriUtilisationReelleTest() {
        List<File> fichiersTest = new ArrayList();
        try {
            fichiersTest.add(new File(
                    getClass().getResource("/utilisationReelle/Original.java")
                            .toURI()));
            fichiersTest.add(new File(
                    getClass().getResource("/utilisationReelle/PasCopie.java")
                            .toURI()));
            fichiersTest.add(new File(
                    getClass().getResource("/utilisationReelle/CopiePlusSubtile.java")
                            .toURI()));
            fichiersTest.add(new File(
                    getClass().getResource("/utilisationReelle/PureCopie.java")
                            .toURI()));
        } catch (URISyntaxException uriException) {
            fail("Erreur lors de la création du URI");
        }

        Task analyse = new Task();
        analyse.setConcatenation(false);

        analyse.setFichiers(fichiersTest);
        analyse.lancerAnalyse();

        if (analys.threadState() == false) {
        MatriceTriangulaire resultats = analyse.getResults();
        Tri.triResultatCroissant(fichiersTest, resultats);

        assertEquals("Original.java", fichiersTest.get(0).getName());
        assertEquals("PureCopie.java", fichiersTest.get(1).getName());
        assertEquals("CopiePlusSubtile.java", fichiersTest.get(2).getName());
        assertEquals("PasCopie.java", fichiersTest.get(3).getName());
        }
    }

   
   
}
