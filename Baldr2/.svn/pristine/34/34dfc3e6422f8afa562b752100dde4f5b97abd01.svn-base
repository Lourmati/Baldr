package ca.qc.bdeb.baldr.ihm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 * Modèle de présentation des résultats sous forme de liste.
 *
 * @author Maxim Bernard
 */
public class ListViewTableModel extends AbstractTableModel {

    private static final String[] COL_NAMES = {
        "File1", "File2", "Result"
    };

    private final List<Entree> listeComps = new ArrayList();
    
    private ResourceBundle messages;
    
    public ListViewTableModel(ResourceBundle messages){
        this.messages = messages;
    }

    /**
     * Une Entree contient deux fichiers et un résultat. Peut être triée en
     * fonction du résultat.
     */
    public class Entree implements Comparable<Entree> {

        private final String nom1, nom2;
        private final File fichier1, fichier2;
        private final double resultat;

        public Entree(File fichier1, File fichier2, String ancetreCommun, double resultat) {
            this.fichier1 = fichier1;
            this.fichier2 = fichier2;

            nom1 = fichier1.getAbsolutePath().replace(ancetreCommun, "");
            nom2 = fichier2.getAbsolutePath().replace(ancetreCommun, "");

            this.resultat = resultat;
        }

        public double getResult() {
            return resultat;
        }

        @Override
        public int compareTo(Entree that) {
            return this.resultat < that.resultat ? -1
                    : this.resultat == that.resultat ? 0 : 1;
        }
    }

    public void addResult(Entree e) {
        listeComps.add(e);
    }

    public File getFirstFileAt(int rowIndex) {
        return listeComps.get(rowIndex).fichier1;
    }

    public File getSecondFileAt(int rowIndex) {
        return listeComps.get(rowIndex).fichier2;
    }

    @Override
    public int getRowCount() {
        return listeComps.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return messages.getString(COL_NAMES[columnIndex]);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Entree e = listeComps.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return e.nom1;
            case 1:
                return e.nom2;
            case 2:
                return e.resultat;
            default:
                throw new RuntimeException("Colonne invalide : " + columnIndex);
        }
    }
}
