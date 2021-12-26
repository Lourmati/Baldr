/*
 * TableCellCustomRenderer.java
 *
 * Created on 12 mai 2007, 16:33
 *$Id: TableCellCustomRenderer.java 234 2007-06-06 21:51:03Z zeta $
 */
package ca.qc.bdeb.baldr.ihm.renderers;

import ca.qc.bdeb.baldr.ihm.TableCell;
import ca.qc.bdeb.baldr.noyau.GestionnairePreferences;
import ca.qc.bdeb.baldr.noyau.Task;
import java.awt.Component;
import java.io.File;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Cell Renderer for the Table
 *
 * @see TableCellRenderer
 * @author zeta
 */
public class TableViewCellCustomRenderer extends AbstractResultCellCustomRenderer {

    private final Task analys;
    private final List<File> tableauFichiersTrie;

    /**
     * Creates a new instance of TableCellCustomRenderer
     * @param min
     * @param max
     * @param nombreDecimal
     * @param analys
     * @param tableauFichiersTrie
     */
  public TableViewCellCustomRenderer(double min, double max, int nombreDecimal,
            Task analys, List<File> tableauFichiersTrie,
            GestionnairePreferences preferences) {
        super(min, max, nombreDecimal,preferences);
        this.analys = analys;
        this.tableauFichiersTrie = tableauFichiersTrie;
    }

    /**
     *
     * @param table
     * @param o
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object o, boolean isSelected, boolean hasFocus, int row, int column) {
        TableCell tableCell = (TableCell) o;
        JLabel reu = (JLabel) rend.getTableCellRendererComponent(table,
                tableCell, isSelected, hasFocus, row, column);

        if (column != 0) {
            if (tableCell != null) {
                float value = tableCell.value;

                writeNumericValueToComponent(reu, value);

                setComponentColorFromValue(reu, value, isSelected);

                if (value == 0) {
                    reu.setToolTipText("Pas analysé");
                } else {
                    writeToolTipText(reu, column, row);
                }
            }
        } else if (tableCell != null) {
            reu.setToolTipText(tableCell.toString());
        }

        return reu;
    }

    /**
     * Crée un infobulle contenant le nom des deux fichiers associés au
     * résultat.
     *
     * @param reu
     * @param columnId
     * @param rowId
     */
    private void writeToolTipText(JLabel reu, int columnId, int rowId) {
        List<File> tabFichiers = analys.getFichiersResultats();

        if (columnId <= tabFichiers.size()) {

            String premierChemin = tabFichiers.get(rowId).toString();
            String deuxiemeChemin = tabFichiers.get(columnId - 1).toString();

            File[] ancetresCommuns = analys.getCommonAncestors();
            if (ancetresCommuns.length == 1) {
                String cheminAncetreCommun = ancetresCommuns[0].getAbsolutePath() + File.separator;

                premierChemin = premierChemin.replace(cheminAncetreCommun, "");

                deuxiemeChemin = deuxiemeChemin.replace(cheminAncetreCommun, "");
            }

            StringBuilder texteToolTip = new StringBuilder();
            texteToolTip.append("<html>");
            texteToolTip.append(premierChemin);
            texteToolTip.append("<br>");
            texteToolTip.append(deuxiemeChemin);
            texteToolTip.append("</html>");

            reu.setToolTipText(texteToolTip.toString());
        }
    }

}
