package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Tests de la classe Projet.
 *
 * @author Maxim Bernard
 */
public class ProjetTest {

    private final String titre = "Test";
    private final String rapport = "ABC";
    private Projet projetTest;
    private Noyau noyau;
    public final ArrayList<File> fichiers;
    public static OuvertureFichier file = new OuvertureFichier();
    private static File fichierBaldr;

    private static final double[][] resultatAttendu = new double[][]{
        {0.0},
        {0.6596974, 0.0},
        {0.86134803, 0.85822636, 0.0}
    };

    public ProjetTest() throws URISyntaxException {
        fichiers = FaireTableauFile();
    }

    public static ArrayList<File> FaireTableauFile() throws URISyntaxException {
        ArrayList<File> fichier = new ArrayList();
        fichier.add(file.Ouverture("/LoremIpsum4.txt"));
        fichier.add(file.Ouverture("/LoremIpsum5.txt"));
        fichier.add(file.Ouverture("/LoremIpsum6.txt"));

        return fichier;
    }

    public static File[] archivedFiles(File[] fichiers) {
        for (int i = 0; i < fichiers.length; i++) {
            fichiers[i] = new File(System.getProperty("user.home")
                    + File.separatorChar
                    + ".baldr"
                    + File.separatorChar
                    + Paths.get(System.getProperty("user.home"))
                            .relativize(fichiers[i].toPath()).toString());
        }
        return fichiers;
    }

    @BeforeClass
    public static void setUpClass() {
        fichierBaldr = new File("src"
                + File.separator + "test"
                + File.separator + "resources"
                + File.separator + "SaveAndRestoreTest.baldr");
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            Files.deleteIfExists(fichierBaldr.toPath());
        } catch (IOException ex) {
        }
    }

    @Before
    public void setUp() {
        noyau = new Noyau();
        projetTest = new Projet(noyau);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void AjouterUntAskSiPasPlein() {
        projetTest.createTask();
        projetTest.createTask();
        projetTest.createTask();
        assertNotNull(projetTest.createTask());
    }

    @Test
    public void AjouterUntAskAvecPosition() {
        projetTest.createTask();
        projetTest.createTask();
        projetTest.createTask();
        projetTest.createTask();
        assertNotNull(projetTest.createTask(2));

    }

    @Test
    public void testEnleverTaskALaideDeTask() {
        projetTest.createTask();
        projetTest.createTask();
        projetTest.createTask();
        projetTest.retirerTask(projetTest.getTasks().get(2));
        assertEquals(projetTest.getTasks().size(), 2);
    }

    @Test
    public void clearTasks() {
        projetTest.createTask();
        projetTest.createTask();
        projetTest.createTask();
        assertEquals(3, projetTest.getTasks().size());
        projetTest.clearTasks();
        assertEquals(0, projetTest.getTasks().size());

    }

    @Test
    public void createTaskTest() {
        Task tache = projetTest.createTask();
        assertTrue(projetTest.getTasks().contains(tache));
    }

    @Test
    public void testDupliquerTask() throws URISyntaxException {
        Task original = projetTest.createTask(0);
        original.setFichiers(fichiers);

        Task resultat = projetTest.duplicateAndRegisterTask(original);

        assertEquals(original.getTousFichiers(), resultat.getTousFichiers());
    }

    @Test
    public void testTrouverIndexeTabTask() throws URISyntaxException {
        Task task1 = projetTest.createTask(0);
        Task task2 = projetTest.createTask(1);

        assertEquals(1, projetTest.findTaskTabIndex(task2));
        assertEquals(0, projetTest.findTaskTabIndex(task1));
    }

    @Test
    public void testSaveRestoreFormatBaldr()
            throws URISyntaxException, SAXException,
            FileNotFoundException, IOException {
        Task task = projetTest.createTask();
        task.setConcatenation(false);

        task.setFichiers(fichiers);
        task.setTitre(titre);
        task.setJReport(rapport);
        task.lancerAnalyse();
        if (task.threadState() == false) {

            try {
                projetTest.save(fichierBaldr);
            } catch (RienASauvegarderException e) {
                fail("Rien à sauvegarder");
            }

            projetTest = new Projet(new Noyau());
            projetTest.restore(fichierBaldr);

            testRestore(projetTest, 0, titre, rapport);
        }
    }

    @Test
    public void testGetLastFile()
            throws URISyntaxException, SAXException,
            FileNotFoundException, IOException {
        projetTest.setLastFile(fichierBaldr);
        assertEquals(projetTest.getLastFile(), fichierBaldr);
    }

    @Test
    public void testFichierModifie() {
        projetTest.createTask();
        assertTrue(projetTest.getModifie());
    }

    /**
     * VÃ©rifie si les tÃ¢ches restaurÃ©es d'un projet correspondent aux
     * rÃ©sultats attends, dans {@link #resultatAttendu}.
     *
     * @param projet Projet Ã  tester.
     * @param task NumÃ©ro de la tÃ¢che Ã  vÃ©rifier.
     * @param titre Titre de la tÃ¢che.
     * @param rapport Rapport de la tÃ¢che (Â«notesÂ»).
     */
    public static void testRestore(Projet projet, int task, String titre, String rapport) {
        float[][] resultat;
        Task taskRestaure = projet.getTasks().get(task);
        assertEquals(titre, taskRestaure.getTitre());
        assertEquals(rapport, taskRestaure.getJReport());
        resultat = taskRestaure.getResults().getValues();

        for (int i = 0; i < resultat.length; i++) {
            for (int j = 0; j < resultat[i].length; j++) {
                assertEquals((float) resultatAttendu[i][j], resultat[i][j], 0);
            }
        }
    }

    @Test
    public void testGetNoyau() {
        assertEquals(noyau, projetTest.getNoyau());
    }

    @Test
    public void testVerifierFichiersDansAnalysesVide() {
        for (int i = 0; i < 10; i++) {
            projetTest.createTask();
        }

        assertFalse(projetTest.verifierFichiersDansAnalyses());
    }

    @Test
    public void testVerifierFichiersDansAnalysesNonVide() {
        for (int i = 0; i < 10; i++) {
            Task tache = projetTest.createTask();
            tache.setFichiers(fichiers);
        }

        assertTrue(projetTest.verifierFichiersDansAnalyses());
    }

    @Test
    public void testVerifierNouveauProjet() {
        projetTest.setLastFile(null);
        assertFalse(projetTest.getModifie());
    }

    @Test
    public void testVerfifierArret() {

        Task tache = projetTest.createTask();
        tache.setFichiers(fichiers);
        tache.lancerAnalyse();
        projetTest.retirerTask(tache);
        tache.stopAnalysis();
        assertEquals(tache.verifierAnalyseFaite(), false);

    }

    /**
     * Verifie qu'un projet avec un sommaire qui a ete sauvegarder et reouvert
     * contient toujours un sommaire "officiel"
     *
     * @throws RienASauvegarderException
     */
    @Test
    public void testSaveAndRestoreSommaire() throws RienASauvegarderException {
        projetTest.createTask();
        projetTest.createTask();
        projetTest.creerTacheSommaire(0);

        File f = new File("test.baldr");

        projetTest.save(f);
        projetTest.restore(f);

        assertTrue(projetTest.getTasks().get(0).estSommaire());
        assertFalse(projetTest.getTasks().get(1).estSommaire());
    }

    @Test
    public void testSetModifie() {
        projetTest.setModifie(true);
        assertTrue(projetTest.getModifie());
    }
}
