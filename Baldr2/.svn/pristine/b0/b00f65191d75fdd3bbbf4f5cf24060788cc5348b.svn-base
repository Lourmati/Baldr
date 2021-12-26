package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author etudiants
 */
public class SommaireTest {

    private Task tache;
    private Noyau noyau;
    private Sommaire sommaire;
    private Projet projetTest;

    @Before
    public void setUp() {
        tache = new Task();
        noyau = new Noyau();
        projetTest = new Projet(noyau);
        List<Task> liste = new ArrayList();
        liste.add(tache);

        sommaire = new Sommaire(liste);
    }

    @Test
    public void estSommaireTest() {
        assertTrue(sommaire.estSommaire());
    }

    @Test
    public void contientTacheTest() {
        assertTrue(sommaire.contientTache(tache));
    }

    @Test
    public void plusieursTasks() {
        Task tache2 = new Task();
        Task tache3 = new Task();
        Task tache4 = new Task();

        List<Task> liste = new ArrayList();
        liste.add(tache);
        liste.add(tache2);
        liste.add(tache3);
        liste.add(tache4);

        sommaire = new Sommaire(liste);

        assertTrue(sommaire.estSommaire());

        assertTrue(sommaire.contientTache(tache));
        assertTrue(sommaire.contientTache(tache2));
        assertTrue(sommaire.contientTache(tache3));
        assertTrue(sommaire.contientTache(tache4));
    }

    @Test
    public void verifierEstTache(){
        assertFalse(tache.estSommaire());
    }

    @Test
    public void verifierTachesComposantes(){
        Task tache2 = new Task();
        Task tache3 = new Task();
        Task tache4 = new Task();

        List<Task> liste = new ArrayList();
        liste.add(tache);
        liste.add(tache2);
        liste.add(tache3);
        liste.add(tache4);

        sommaire = new Sommaire(liste);

        sommaire.tachesComposantes.add(tache);

        assertEquals(5, sommaire.tachesComposantes.size());
        assertFalse(sommaire.tachesComposantes.get(0).estSommaire());
    }

    @Test
    public void testAjoutTachesContenantMemeFichiersConcatenantion(){
        List<File> tabFichiers;
        List<File> tabFichiersRes = new ArrayList();
         tabFichiersRes.add(new File("/home/user/fic1"));
        tabFichiersRes.add(new File("/home/user/fic2"));
        tabFichiersRes.add(new File("/home/user/rep1/fic3"));
        
      
        Task task1 = projetTest.createTask();
        tabFichiers = new ArrayList();
        tabFichiers.add(tabFichiersRes.get(0));
        tabFichiers.add(tabFichiersRes.get(1));
        tabFichiers.add(tabFichiersRes.get(2));
        task1.setFichiers(tabFichiers);
        
        Task task2 = projetTest.createTask();
        tabFichiers.clear();
        tabFichiers.add(tabFichiersRes.get(0));
        tabFichiers.add(tabFichiersRes.get(1));
        tabFichiers.add(tabFichiersRes.get(2));
        task1.setFichiers(tabFichiers);
        
        sommaire =
                projetTest.creerTacheSommaire(projetTest.getTasks().size());
        
        assertEquals(3,sommaire.getTousFichiers().size());
    }
    
    @Test
    public void testSuppressionTask(){
        Task tache2 = new Task();
        Task tache3 = new Task();
        Task tache4 = new Task();

        List<Task> liste = new ArrayList();
        liste.add(tache);
        liste.add(tache2);
        liste.add(tache3);
        liste.add(tache4);
        sommaire = new Sommaire(liste);
        
        assertEquals(4, sommaire.tachesComposantes.size());
        
        sommaire.tachesComposantes.remove(tache);
        
        assertFalse(sommaire.contientTache(tache));
        assertTrue(sommaire.contientTache(tache2));
        assertTrue(sommaire.contientTache(tache3));
        assertTrue(sommaire.contientTache(tache4));
        
        assertEquals(3, sommaire.tachesComposantes.size());
        
          for (int i = 0; i < 2; i++) {
            sommaire.tachesComposantes.remove(i);
        }
          
        assertEquals(1, sommaire.tachesComposantes.size());
        
        assertFalse(sommaire.contientTache(tache2));
        assertFalse(sommaire.contientTache(tache4));
        assertTrue(sommaire.contientTache(tache3));
    }
    
    
    @Test
    public void testCreationTacheSommaire() {
        List<File> tabFichiers;
        tabFichiers = new ArrayList();
        
        List<File> tabFichiersRes = new ArrayList();
        tabFichiersRes.add(new File("/home/user/fic1"));
        tabFichiersRes.add(new File("/home/user/fic2"));
        tabFichiersRes.add(new File("/home/user/rep1/fic3"));
        tabFichiersRes.add(new File("/home/user/fic3"));
        tabFichiersRes.add(new File("/home/user/fic6"));
        tabFichiersRes.add(new File("/home/user/rep1/fic5"));
       

        Task task1 = projetTest.createTask();
        tabFichiers = new ArrayList();
        tabFichiers.add(tabFichiersRes.get(0));
        tabFichiers.add(tabFichiersRes.get(1));
        tabFichiers.add(tabFichiersRes.get(5));
        task1.setFichiers(tabFichiers);

        tabFichiers.clear();
        Task task2 = projetTest.createTask();
        tabFichiers.add(tabFichiersRes.get(0));
        tabFichiers.add(tabFichiersRes.get(3));
        tabFichiers.add(tabFichiersRes.get(2));
        task2.setFichiers(tabFichiers);

        tabFichiers.clear();
        Task task3 = projetTest.createTask();
        tabFichiers.add(tabFichiersRes.get(4));
        tabFichiers.add(tabFichiersRes.get(1));
        tabFichiers.add(tabFichiersRes.get(5));
        task3.setFichiers(tabFichiers);

        sommaire =
                projetTest.creerTacheSommaire(projetTest.getTasks().size());

        assertFalse(sommaire.contientTache(tache));
        assertTrue(sommaire.contientTache(task1));
        assertTrue(sommaire.contientTache(task2));
        assertTrue(sommaire.contientTache(task3));

        for (int i = 0; i < sommaire.getTousFichiers().size(); i++) {
            assertTrue(sommaire.getTousFichiers().contains(tabFichiersRes.get(i)));
        }      
    
        
        assertEquals(sommaire, projetTest.getTacheSommaire());
    }
    
}