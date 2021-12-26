/*
 * ResTableMouseAdapter.java
 *
 * Created on 6 juin 2007, 21:56
 *$Id$
 */
package ca.qc.bdeb.baldr.ihm;

import ca.qc.bdeb.baldr.noyau.Noyau;
import ca.qc.bdeb.baldr.utils.ArrayUtil;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JTable;

/**
 *
 * @author zeta
 */
public class ResTableMouseAdapter extends MouseAdapter {

    public JTable table;
    private Noyau noyau = null;

    /**
     * Creates a new instance of ResTableMouseAdapter
     *
     * @param tab
     * @param noyau
     */
    public ResTableMouseAdapter(JTable tab, Noyau noyau) {
        super();
        this.table = tab;
        this.noyau = noyau;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
            Point pt = new Point(e.getX(), e.getY());
            int row = table.rowAtPoint(pt);
            int col = table.columnAtPoint(pt) - 1;

            if ((Boolean) noyau.getPrefs().readPref("VUE_COURAN", false)) {
                if (col == row || col == -1) {
                    return;
                }
            }

            String f1, f2;

            if (table.getModel() instanceof BaldrTableModel) {
                BaldrTableModel tableauBaldr = (BaldrTableModel) table.getModel();
                if (table.getRowSorter() != null) {
                    String colName = table.getColumnName(col + 1);
                    col = Integer.parseInt(colName.substring(0, colName.indexOf(" "))) - 1;
                    row = table.convertRowIndexToModel(row);
                } else {
                    col = table.convertColumnIndexToModel(col);
                }

                f1 = tableauBaldr.getColumnFile(col).getAbsolutePath();
                f2 = tableauBaldr.getRowFile(row).getAbsolutePath();
            } else {
                ListViewTableModel model = (ListViewTableModel) table.getModel();
                f1 = model.getFirstFileAt(row).getAbsolutePath();
                f2 = model.getSecondFileAt(row).getAbsolutePath();
            }

            String comparator = noyau.getPrefs().readPref("COMPARATOR");

            if ((System.getProperty("os.name").toUpperCase().contains("MAC"))
                    && (comparator.replace("$1", "").replace("$2", "").trim().endsWith(".app"))) {
                comparator = "open -a " + comparator;
            }

            if (comparator.length() < 1) {
                ErrorMessages.noComparatorDefined();
                return;
            }

            if (!comparator.contains("$1")) {
                comparator += " $1";
            }

            if (!comparator.contains("$2")) {
                comparator += " $2";
            }

            try {
                String[] args = replaceDollarSignsInComparatorString(comparator, f1, f2);
                Runtime.getRuntime().exec(ArrayUtil.trimStringsInArray(args));
            } catch (IOException ex) {
                ErrorMessages.cannotExecute();
            }
        }
    }

    /**
     * À partir d'une chaîne "a$1b$2c", donne un tableau {a, $1, b, $2, c}, en
     * remplaçant $1 et $2 par deux noms ou chemins de fichiers.
     *
     * @param comparator Chaîne d'entrée.
     * @param file1
     * @param file2
     * @return
     */
    private static String[] replaceDollarSignsInComparatorString(String comparator,
            String file1, String file2) {
        String[] arr = comparator.split("\\$1");

        if (arr[0].contains("$2")) {
            String[] newArr = arr[0].split("\\$2");
            arr = new String[]{newArr[0], newArr[1], (arr.length > 1 ? arr[1] : null)};
        } else {
            String[] newArr = arr[1].split("\\$2");
            arr = new String[]{arr[0], newArr[0], (newArr.length > 1 ? newArr[1] : null)};
        }

        if (arr[2] != null) {
            return new String[]{arr[0], file1, arr[1], file2, arr[2]};
        } else {
            return new String[]{arr[0], file1, arr[1], file2};
        }
    }

}
