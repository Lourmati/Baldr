package ca.qc.bdeb.baldr.noyau;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Une tache sommaire, c'est une tache qui contient les résultats des autres
 * taches
 *
 * @author mpauze
 */
public class Sommaire extends Task {

    /**
     * Rien de spéciale, s'initalise comme sa {@link Task#Task() classe mère}
     */
    public Sommaire() {
        super();
    }

    /**
     *
     * @param tachesComposantes
     */
    public Sommaire(List<Task> tachesComposantes) {
        this.tachesComposantes = new ArrayList(tachesComposantes);
    }

    /**
     * Retourne true, un sommaire est un sommaire
     *
     * @return {@code true}
     */
    @Override
    public boolean estSommaire() {
        return true;
    }

    /**
     * Détermine si la Sommaire contient la tache
     *
     * @param tache
     * @return true si le sommaire contient la tache
     */
    @Override
    public boolean contientTache(Task tache) {
        return tachesComposantes.contains(tache);
    }

    /**
     * Méthode qui retourne une liste de tous les fichiers qui doivent
     * être dans le sommaire.
     *
     * @return
     */
    public List<File> creationFichierSommaire() {
        List<File> allFile = new ArrayList();

        for (Task task : tachesComposantes) {            
            List<File> taskFile = task.getTousFichiers();
            if (taskFile != null) {
                for (File fichier : taskFile) {
                    if (verifierDoublonsSommaire(allFile, fichier)) {
                        allFile.add(fichier);
                    }
                }
            }
        }
        
        return allFile;
    }
    
    /*
    * Méthode qui vérifie dans pour chaque élément de la liste de Fichiers
    * à la recherche d'un fichier doublon qui devrait être enlevé pour ne pas
    * l'afficher deux fois.
    */
    private boolean verifierDoublonsSommaire(List<File> allFile, File aAjouter) {
        boolean ajoutable = true;

        for (File file : allFile) {
            if (aAjouter.equals(file)) {
                ajoutable = false;
                break;
            }
        }
        return ajoutable;
    }

}
