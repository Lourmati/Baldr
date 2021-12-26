/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.baldr.noyau;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author 1647233
 */
public class GestionnaireFiltresTest {

    GestionnaireFiltres gestFiltre;
    public GestionnaireFiltresTest() {
    }
    
    @Before
    public void setUp() {
        gestFiltre = new GestionnaireFiltres();
    }
    
    @After
    public void tearDown() {
        gestFiltre = null;
    }
    
    @Test
    public void testConstructeurVide() {
        
    }
    
    @Test
    public void testAjouterExclureFiltre() {
        gestFiltre.exclureFiltre("*.java");
        assertEquals(gestFiltre.getFiltresExclu().length, 1);
    }
    
    @Test
    public void testEnleverExclureFiltre() {
        gestFiltre.exclureFiltre("*.java");
        gestFiltre.suprimerFiltreExclu("*.java");
        assertTrue(gestFiltre.isEmpty());
    }
    
    @Test
    public void testGetFiltres() {
        gestFiltre.exclureFiltre("*.java");
        assertTrue((gestFiltre.getFiltresExclu())[0].equals("*.java"));
    }
    
}
