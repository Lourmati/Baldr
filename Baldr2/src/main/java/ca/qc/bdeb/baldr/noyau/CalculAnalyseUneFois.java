/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Shajaan
 */
public class CalculAnalyseUneFois {

    File f1;
    File f2;
    double resultat;
    Task tache;
    Task.FilePair files;

    public Task getTache() {
        return tache;
    }

    public Task.FilePair getFiles() {
        return files;
    }

    public void remplirListeFichier(File f1, File f2) {
        
        HashMap<File, File> fichierDejaFait = new HashMap<File, File>();
        fichierDejaFait.put(getFiles().getFile1(), getFiles().getFile2());
    }

    public void remplirListeResultat(double resultat) {
        
        List<MatriceTriangulaire> resultatDejacalculer = new ArrayList<>();
        resultatDejacalculer.add(tache.getResults());
    }
}
