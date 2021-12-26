/*
 * Main.java
 *
 * Created on 10 avril 2007, 14:42
 *$Id: Main.java 255 2007-09-23 15:34:43Z zeta $
 */
package ca.qc.bdeb.baldr.main;

import ca.qc.bdeb.baldr.ihm.ConsoleIhm;
import ca.qc.bdeb.baldr.ihm.WindowBaldr;
import ca.qc.bdeb.baldr.noyau.GestionnaireFiltres;
import ca.qc.bdeb.baldr.noyau.Noyau;
import java.io.File;

/**
 * Le point d'entrée du programme, utilisé pour lier l'interface graphique au
 * noyau.
 *
 * @author Baldr Team
 */
public final class Main {

    /**
     * Le nombre d'onglets maximal supporté par le programme.
     */
    public static final int MAXONGLET = 10;

    /**
     * La fenêtre du programme.
     */
    public static WindowBaldr ihm;

    private static Noyau noyau;
    private static GestionnaireFiltres filtre;

    /**
     * Séparateur de fichiers dans un chemin, pour être utilisé dans les
     * expressions régulières ou dans {@link String#split(java.lang.String)}.
     */
    public static final String regexFileSeparator;

    static {
        if (File.separator.equals("\\")) {
            regexFileSeparator = "\\\\";
        } else {
            regexFileSeparator = File.separator;
        }
    }

    /**
     * Vrai si le système d'exploitation est UNIX ou l'un de ses dérivés.
     */
    public static final boolean isUnix;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        isUnix = os.contains("nux") || os.contains("nix");
    }

    /**
     * Vrai si le système d'exploitation est Mac OS X.
     */
    public static final boolean isMac;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        isMac = os.contains("mac");
    }

    /**
     * La fonction principale du programme.
     *
     * @param args Les arguments passés au programme.
     */
    public static void main(final String args[]) {
        noyau = new Noyau();
        filtre = new GestionnaireFiltres(noyau);
        filtre.obtenirFiltresAjout();
        filtre.obtenirFiltresExclu();

        if (args.length > 0 && args[0].equals("-")) {
            int i = 0;
            
            ConsoleIhm ihmc = new ConsoleIhm(noyau.getPrefs());

            for (String arg : args) {
                if (i != 0) {
                    File f = new File(arg);

                    if (f.exists()) {
                        ihmc.addFile(f);
                    }
                }

                i++;
            }

            if (i != 1) {
                ihmc.go();
            } else {
                System.err.println("No files ...");
            }
        } else {
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ihm = new WindowBaldr(noyau, filtre);

                    if (args.length > 0) {
                        for (String arg : args) {
                            File f = new File(arg);

                            if (f.exists()) {
                                noyau.getProjetCourant().restore(f);
                            }
                        }
                    }

                }
            });
        }
    }
}
