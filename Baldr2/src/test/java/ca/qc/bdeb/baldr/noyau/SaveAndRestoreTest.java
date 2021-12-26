package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author
 */
public class SaveAndRestoreTest {

    private static final String titre = "Ajouter a un fichier";
    private static final String rapport = "DEF";

    private static File fichierBaldr;

    public SaveAndRestoreTest() {
    }

    @BeforeClass
    public static void setUpClass() throws URISyntaxException {
        fichierBaldr = new File("src"
                + File.separator + "test"
                + File.separator + "resources"
                + File.separator + "ExportToTest.baldr");

        // NOTE(pascal) : Copie de la création d'un fichier présente
        // dans ProjetTest.java (testSaveAndRestoreFormatBaldr()).
        Projet projetTest = new Projet(new Noyau());
        Task task = projetTest.createTask();
        task.setConcatenation(false);

        task.setFichiers(ProjetTest.FaireTableauFile());
        task.setTitre("Test");
        task.setJReport("ABC");
        task.lancerAnalyse();
        try{
            projetTest.save(fichierBaldr);
        }catch (RienASauvegarderException e){
               
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            Files.deleteIfExists(fichierBaldr.toPath());
        } catch (IOException ex) {
            System.err.println("[ERREUR] Erreur à la suppression de "
                    + fichierBaldr.getPath() + " : " + ex.getLocalizedMessage());
        }
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test(expected = RienASauvegarderException.class)
    public void testSauvegarderFichierNull() throws RienASauvegarderException {
        SaveAndRestore saveAndRestore = new SaveAndRestore(null);
        saveAndRestore.save(fichierBaldr);
        fail("Aucune erreur n'a été lancée");
    }

    @Test
    public void testExportTo() throws FileNotFoundException, IOException, URISyntaxException {
        // NOTE(pascal) : Réécrire ce test pour être autonome. En ce moment,
        // il dépend d'un fichier créé dans ProjetTest.java (dont j'ai
        // recopié le code plus haut, dans le contexte d'un premier pas
        // vers l'autonomie du test).
        Task task = new Task();
        task.setConcatenation(false);

        task.setFichiers(ProjetTest.FaireTableauFile()); // FIXME : est public !
        task.setTitre(titre);
        task.setJReport(rapport);
        task.lancerAnalyse();
        if (task.threadState() == false) {    
        SaveAndRestore.exportTo(task, fichierBaldr);

        Projet projetTest = new Projet(new Noyau());
        projetTest.restore(fichierBaldr);
        
        ProjetTest.testRestore(projetTest, 0, "Test", "ABC");
        ProjetTest.testRestore(projetTest, 3, titre, rapport);
        }
    }
}
