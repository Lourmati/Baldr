/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.baldr.noyau;

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
public class MatriceTriangulaireTest {

    /**
     * Objet utilisé pour les tests.
     */
    private MatriceTriangulaire res;

    public MatriceTriangulaireTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        res = new MatriceTriangulaire(4);
        float[][] valeursDebut = {
            {1},
            {2, 3},
            {4, 5, 6},
            {7, 8, 9, 10}
        };

        for (int i = 0; i < valeursDebut.length; i++) {
            for (int j = 0; j < valeursDebut[i].length; j++) {
                res.setRes(i, j, valeursDebut[i][j]);
            }
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void TestCas1() {
        MatriceTriangulaire res = new MatriceTriangulaire(4);

        int x = 0;
        int y = 0;

        for (int i = 1; i < 11; i++) {
            res.setRes(x, y, i);
            if (x == y) {
                y = 0;
                x++;
            } else {
                y++;
            }
        }
        MatriceTriangulaire resAttendu = new MatriceTriangulaire(4);
        resAttendu.setRes(0, 0, 1);

        resAttendu.setRes(1, 0, 4);

        resAttendu.setRes(1, 1, 6);
        resAttendu.setRes(2, 0, 2);
        resAttendu.setRes(2, 1, 5);
        resAttendu.setRes(2, 2, 3);
        resAttendu.setRes(0, 3, 7);
        resAttendu.setRes(3, 1, 9);
        resAttendu.setRes(3, 2, 8);
        resAttendu.setRes(3, 3, 10);

        res.echangerValeurs(2, 1);

        x = 0;
        y = 0;
        for (int i = 1; i < 11; i++) {
            assertEquals((int) resAttendu.getResAt(x, y), (int) res.getResAt(x, y));
            if (x == y) {
                y = 0;
                x++;
            } else {
                y++;
            }
        }

    }

    @Test
    public void TestCas2() {
        MatriceTriangulaire res = new MatriceTriangulaire(4);

        int x = 0;
        int y = 0;

        for (int i = 1; i < 11; i++) {
            res.setRes(x, y, i);
            if (x == y) {
                y = 0;
                x++;
            } else {
                y++;
            }
        }
        MatriceTriangulaire resAttendu = new MatriceTriangulaire(4);
        resAttendu.setRes(0, 0, 1);

        resAttendu.setRes(1, 0, 7);

        resAttendu.setRes(1, 1, 10);
        resAttendu.setRes(2, 0, 4);
        resAttendu.setRes(2, 1, 9);
        resAttendu.setRes(2, 2, 6);
        resAttendu.setRes(3, 0, 2);
        resAttendu.setRes(3, 1, 8);
        resAttendu.setRes(3, 2, 5);
        resAttendu.setRes(3, 3, 3);

        res.echangerValeurs(1, 3);

        x = 0;
        y = 0;
        for (int i = 1; i < 11; i++) {
            assertEquals((int) resAttendu.getResAt(x, y), (int) res.getResAt(x, y));
            if (x == y) {
                y = 0;
                x++;
            } else {
                y++;
            }
        }

    }

    @Test
    public void testerCompterAnalyse() {
        MatriceTriangulaire res = new MatriceTriangulaire(4);

        int x = 0;
        int y = 0;

        for (int i = 1; i < 11; i++) {
            res.setRes(x, y, i);
            if (x == y) {
                y = 0;
                x++;
            } else {
                y++;
            }
        }
        assertEquals(0, res.compterAnalyse());
        res.setRes(0, 1, -1);

        assertEquals(1, res.compterAnalyse());
    }

    @Test
    public void testEnleverLigneEtColonne() {
        float[][] valeursFin = {
            {1},
            {4, 6},
            {7, 9, 10}
        };

        res.enleverLigneEtColonne(1);

        for (int i = 0; i < valeursFin.length; i++) {
            for (int j = 0; j < valeursFin[i].length; j++) {
                assertEquals(valeursFin[i][j], res.getRes(i, j), 0);
            }
        }
    }

    @Test
    public void testGetMinMaxValues() {

        float[] minMax = res.getMinMaxValues();
        assertEquals(1, minMax[0], 0);
        assertEquals(10, minMax[1], 0);
    }

    @Test
    public void testGetMinMaxValuesEquals0() {
        float[][] valeursDebut = {
            {0},
            {0, 0},
            {0, 0, 0},
            {0, 0, 0, 0}
        };

        for (int i = 0; i < valeursDebut.length; i++) {
            for (int j = 0; j < valeursDebut[i].length; j++) {
                res.setRes(i, j, valeursDebut[i][j]);
            }
        }

        float[] minMax = res.getMinMaxValues();
        assertEquals(0, minMax[0], 0);
        assertEquals(0, minMax[1], 0);
    }

    @Test
    public void testGetMinMaxValuesDesc() {

        float[][] valeursDebut = {
            {10},
            {9, 8},
            {7, 6, 5},
            {4, 3, 2, 1}
        };

        for (int i = 0; i < valeursDebut.length; i++) {
            for (int j = 0; j < valeursDebut[i].length; j++) {
                res.setRes(i, j, valeursDebut[i][j]);
            }
        }

        float[] minMax = res.getMinMaxValues();
        assertEquals(1, minMax[0], 0);
        assertEquals(10, minMax[1], 0);
    }
}
