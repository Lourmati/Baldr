/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.baldr.utils;

import ca.qc.bdeb.baldr.noyau.OuvertureFichier;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maxim Bernard
 */
public class CorrespondanceTest {

    private List<File> fichiers;
    private static OuvertureFichier file = new OuvertureFichier();
    
    private static final String strFichErreur = "Erreurs.txt";
    private static final String initStrIndexOf = "abcdefghijk";

    public CorrespondanceTest() throws URISyntaxException {
        fichiers = FaireTableauFile();
    }
    
    public final List<File> FaireTableauFile() throws URISyntaxException {

        List<File> fichier = new ArrayList();
        fichier.add(file.Ouverture("/CorrTest.txt"));
        return fichier;
    }
    @BeforeClass
    public static void setUpClass() throws URISyntaxException {
        
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testStringMatchesPatternCas1() {
        String pattern = "*.txt";
        assertTrue(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }

    @Test
    public void testStringMatchesPatternCas2() {
        String pattern = "*.???";
        assertTrue(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }

    @Test
    public void testStringMatchesPatternCas3() {
        String pattern = "CorrTest.*";
        assertTrue(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }

    @Test
    public void testStringMatchesPatternCas4() {
        String pattern = "CorrTest.????";
        assertFalse(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }

    @Test
    public void testStringMatchesPatternCas5() {
        String pattern = "?.*";
        assertFalse(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }
    
    @Test
    public void testStringMatchesPatternCas6() {
        String pattern = "*CorrTest.txt";
        assertTrue(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }
    
    @Test
    public void testStringMatchesPatternCas7() {
        String pattern = "CorrTest.txt*";
        assertTrue(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }
    
    @Test
    public void testStringMatchesPatternCas8() {
        String pattern = "????????.???";
        assertTrue(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }
    
    @Test
    public void testStringMatchesPatternCasExact() {
        String pattern = "CorrTest.txt";
        assertTrue(Correspondance.stringMatchesPattern(fichiers.get(0), pattern));
    }
    
    

    @Test
    public void testIndexOfSaufCaracteresInconnusCas1() {
        String queryStr = "de??hi";
        int index = Correspondance.indexOfSaufCaracteresInconnus(initStrIndexOf, queryStr);

        assertEquals(3, index);
    }

    @Test
    public void testIndexOfSaufCaracteresInconnusCas2() {
        String queryStr = "??cd";
        int index = Correspondance.indexOfSaufCaracteresInconnus(initStrIndexOf, queryStr);

        assertEquals(0, index);
    }

    @Test
    public void testIndexOfSaufCaracteresInconnusCas3() {
        String queryStr = "ghijklmn";
        int index = Correspondance.indexOfSaufCaracteresInconnus(initStrIndexOf, queryStr);

        assertEquals(-1, index);
    }

    @Test
    public void testIndexOfSaufCaracteresInconnusCas5() {
        String queryStr = "ppppppp";
        int index = Correspondance.indexOfSaufCaracteresInconnus(initStrIndexOf, queryStr);

        assertEquals(-1, index);
    }

}
