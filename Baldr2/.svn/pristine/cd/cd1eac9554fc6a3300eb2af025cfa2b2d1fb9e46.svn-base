/*
 * TableHeaderCellCustomRenderer.java
 *
 * Created on 6 mai 2007, 19:35
 *$Id: TableHeaderCellCustomRenderer.java 227 2007-06-05 15:32:57Z zeta $
 */
package ca.qc.bdeb.baldr.ihm.renderers;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * Cell Renderer for the Table header and first column
 *
 * @see TableCellRenderer
 * @author zeta
 */
public class TableHeaderCellCustomRenderer implements TableCellRenderer {

    /**
     * Creates a new instance of TableHeaderCellCustomRenderer
     */
    private TableCellRenderer rend;

    private class UIResourceTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();
                if (header != null) {
                    setForeground(header.getForeground());
                    setBackground(header.getBackground());
                    setFont(header.getFont());
                }
            }

            setText((value == null) ? "" : value.toString());
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return this;
        }
    }

    public TableHeaderCellCustomRenderer() {
        rend = new UIResourceTableCellRenderer();

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int i;
        DefaultTableCellRenderer reu = (DefaultTableCellRenderer) rend.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        StringBuffer str = new StringBuffer();
        String s = value.toString();
        String[] sSplit;

        if (column != 0) {
            String s2 = "";
            for (char c : value.toString().toCharArray()) {
                if (c >= '0' && c <= '9') {
                    s2 += c;
                } else {
                    break;
                }
            }
            sSplit = s.split("\n");
            reu.setText(s2);
            reu.setIcon(null);
        } else {
            sSplit = s.split("\n");

            reu.setText(sSplit[0]);
            reu.setIcon(null);
        }

        if (sSplit.length == 2) {
            reu.setToolTipText(sSplit[1]);
        } else {
            reu.setToolTipText(sSplit[0]);
        }

        if (isSelected) {
            reu.setBackground(table.getSelectionBackground());
            reu.setForeground(table.getSelectionForeground());
        }
        return reu;
    }
}
