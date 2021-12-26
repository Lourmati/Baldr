/*
 * ConsoleIhm.java
 *
 * Created on 23 septembre 2007, 16:20
 *$Id$
 */
package ca.qc.bdeb.baldr.ihm;

import ca.qc.bdeb.baldr.noyau.GestionnairePreferences;
import ca.qc.bdeb.baldr.noyau.Task;

import java.util.ArrayList;
import java.io.File;
import java.util.List;

/**
 *
 * @author zeta
 */
public class ConsoleIhm {
    /** Les fichiers de l'analyse. */
    private List<File> files;
    
    /** La tâche à exécuter. */
    private Task task;
    
    /**
     * Créée une nouvelle instance de ConsoleIhm.
     * @param prefs Les préférences associées à Baldr.
     */
    public ConsoleIhm(GestionnairePreferences prefs) {
        files = new ArrayList();
        task = new Task();
        task.setPrefs(prefs);
    }

    /**
     * Ajoute un fichier à analyser.
     * @param f Le fichier.
     */
    public void addFile(File f) {
        files.add(f);
    }

    /**
     * Lance l'analyse.
     */
    public void go() {
        task.setFichiers(files);
        task.lancerAnalyse();
    }
}
