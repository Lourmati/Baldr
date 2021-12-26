/*
 * BaldrTableModel.java
 *
 * Created on 5 juin 2007, 18:06
 *$Id$
 */
package ca.qc.bdeb.baldr.ihm;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author zeta
 */
public class BaldrTableModel extends AbstractTableModel {
    // TODO: Commenter et clarifier le code en générale

    /**
     * Creates a new instance of BaldrTableModel
     */
    private float[][] data;
    private boolean[][] done;

    private List<File> files;
    private String[] headings;
    /**
     * Déterminer l'utilité de moy
     */
    private boolean moy = true;
    private ResourceBundle messages;

    /**
     *
     * @param files La liste des fichiers à mettre dans le tableaux
     * @param data Résultats à afficher dans le tableau
     * @param messages Le {@link ResourceBundle ResourceBundle} des
     * messages(traductions)
     */
    public BaldrTableModel(
            List<File> files, float[][] data, ResourceBundle messages) {
        this(files, data, true, messages);
    }

    /**
     *
     * @param files La liste des fichiers à mettre dans le tableaux
     * @param data Résultats à afficher dans le tableau
     * @param moy ???
     * @param messages Le {@link ResourceBundle ResourceBundle} des
     * messages(traductions)
     */
    public BaldrTableModel(List<File> files, float[][] data, boolean moy,
            ResourceBundle messages) { //TODO: découper en méthodes

        this.messages = messages;
        String[] titres = new String[files.size() + 1];

        //pre-calculate headings
        headings = new String[files.size() + 1];
        for (int j = 0; j < files.size(); j++) {
            titres[j + 1] = (j + 1) + " - " + files.get(j).getName();
        }

        for (int i = 1; i <= files.size(); i++) {
            headings[i] = titres[i] + "\n" + files.get(i - 1).getAbsolutePath();
        }

        headings[0] = this.messages.getString("Files");
        // setting attributes
        this.files = files;
        this.data = new float[this.files.size()][this.files.size()];
        for (int i = 0; i < this.files.size(); i++) {
            for (int j = 0; j < this.files.size(); j++) {
                this.data[i][j] = valRead(data, i, j);
            }
        }
        this.done = new boolean[this.files.size()][this.files.size()];
        this.moy = moy;
    }

    /**
     *
     * @return Le nombre de rangées dans le tableau de résultats
     */
    @Override
    public int getRowCount() {
        return getData().length;
    }

    /**
     *
     * @return Le nombre de colonnes dans le tableau de résultats
     */
    @Override
    public int getColumnCount() {
        return files.size() + 1;
    }

    /**
     * FIXME: Il n'y a pas de différence dans le code des deux méthodes, il faut
     * donc trouver ce qu'ils font et les modifier
     */
    /**
     * Retourne le {@link File fichier} trouvé dans la colonne passé en
     * paramètre
     *
     * @param columnIndex L'indice de la colonne dont on veut avoir
     * l'information
     * @return Le {@link File fichier} dans la colonne
     */
    public File getColumnFile(int columnIndex) {
        return files.get(columnIndex);
    }

    /**
     * Retourne le {@link File fichier} trouvé dans la rangée passé en paramètre
     *
     * @param rowIndex L'indice de la rangé dont on veut avoir l'information
     * @return Le {@link File fichier} dans la rangé
     */
    public File getRowFile(int rowIndex) {
        return files.get(rowIndex);
    }

    /**
     *
     * @param columnIndex
     * @return Un String de l'entête à l'indice spécifié
     */
    @Override
    public String getColumnName(int columnIndex) { //TODO : Accord avec tri
        return headings[columnIndex];
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return Un string de l'entête qui est à l'endroit spécifié OU un
     * {@link TableCell TableCell} de ce qui est à rowIndex et columnIndex
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return headings[rowIndex + 1];
        }

        return new TableCell(data[rowIndex][columnIndex - 1],
                done[rowIndex][columnIndex - 1]);
    }

    private float valRead(float[][] tab, int i, int j) {
        if (i == j) {
            return 0;
        }
        if (tab[i].length > j) {
            return tab[i][j];
        } else {
            return tab[j][i];
        }
    }

    private class MargEl implements Comparable {

        private int rank;
        private float value;

        public MargEl(int r, float val) {
            rank = r;
            value = val;
        }

        @Override
        public int compareTo(Object o) {

            float v = ((MargEl) o).getValue();
            if (v > value) {
                return -1;
            }
            return 1; // never equals ( loosing obj else)

        }

        public float getValue() {
            return value;
        }

        public int getRank() {
            return rank;
        }

    }

    /**
     * Retourne un tableau de float de toutes les résultats dans le tableau
     * comme ils sont affiché
     *
     * @return Un tableau de float contenant les résultats comme affiché dans le
     * tableau
     */
    public float[][] getData() {
        return data;
    }

    /**
     * Retourne un tableau des chemins absolu des fichiers qui sont dans le
     * tableau des résultats. Quelque chose qui a l'air comme 
     * "C:\Users\Matt\Documents\NetBeansProjects\Baldr\src\main\java\ca\qc\bdeb\
     * baldr\ihm\APropos.java 2 - AbstractResultCellCustomRenderer.java"
     *
     * @return Un tableau de strings des entêtes à droite dans le tableau
     */
    public String[] getHeadings() {
        return headings;
    }

}
