/*
 * Extension.java
 *
 * Created on 14 mai 2007, 11:57
 *$Id: Extension.java 200 2007-05-26 23:02:02Z zeta $
 */
package ca.qc.bdeb.baldr.utils;

import java.io.File;

/**
 * Gère les extensions des fichiers de sauvegarde .baldr(x).
 *
 * @author zeta
 */
public class Extension {

    /**
     * L'extension de fichier baldr non compressé.
     */
    public final static String BALDR = "baldr";

    /**
     * Retourne l'extension d'un fichier.
     *
     * @param f Le fichier duquel on veut savoir l'extension de fichier.
     * @return L'extension de fichier (excluant le «.»), ou null si aucune.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();

        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }

        return ext;
    }
    
    public static boolean contientExtensionValide(File fichier) {
        String extension = getExtension(fichier);
        boolean estValide = false;
        if (extension.equalsIgnoreCase(BALDR)) {
            estValide = true;
        }
        return estValide;
    }

}
