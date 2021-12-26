
package ca.qc.bdeb.baldr.formattage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Pascal Laprade
 */
public class CommentParserTest {
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void getOutputTestCSharp() {
        getOutputTestUniversal("/commentaires/csharp-avec.cs",
                "/commentaires/csharp-sans.cs");
    }
    
    @Test
    public void getOutputTestPython() {
        getOutputTestUniversal("/commentaires/python-avec.py",
                "/commentaires/python-sans.py");
    }
    
    @Test
    public void getOutputTestAssembleur() {
        getOutputTestUniversal("/commentaires/assembleur-avec.asm",
                "/commentaires/assembleur-sans.asm");
    }
    
    @Test
    public void getOutputTestSQL() {
        getOutputTestUniversal("/commentaires/sql-avec.sql",
                "/commentaires/sql-sans.sql");
    }
    
    /*
    Méthode universelle qui éxécute le test selon le type de language passé en
    paramètre. Ceci est effectué dans le but d'éviter 4 méthodes contenant
    quasiment le même code à l'exception de 2 lignes.
    */
    public void getOutputTestUniversal(String langAvant, String langApres) {
        File avant = null;
        File apres = null;
        
        try {
            avant =
                new File(getClass().getResource(langAvant)
                        .toURI());
            
            apres =
                new File(getClass().getResource(langApres)
                        .toURI());
        } catch (URISyntaxException uriException) {
            fail("Erreur lors de la création du URI");
        }
        
        StringBuilder sortie = new StringBuilder();
        StringBuilder comparaison = new StringBuilder();
        
        BufferedInputStream in = null;
        BufferedInputStream comp = null;
        try {
            in = new BufferedInputStream(new FileInputStream(avant));
            
            CommentParser parser = new CommentParser(avant);
            
            int car;
            while ((car = in.read()) != -1) {
                parser.lireCaractere((char)car);
                sortie.append(parser.retournerCaractereChaine());
            }
            
            comp = new BufferedInputStream(new FileInputStream(apres));
            
            while((car = comp.read()) != -1) {
                comparaison.append((char)car);
            }
        } catch (IOException ex) {
            fail("Impossible d'ouvrir le fichier (IOException).");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                
                if (comp != null) {
                    comp.close();
                }
            } catch (IOException ex) {
                
            }
        }

        String comparaisonFinal = comparaison.toString().replaceAll("\\s+","");
        assertEquals(comparaisonFinal, sortie.toString());
    }
}
