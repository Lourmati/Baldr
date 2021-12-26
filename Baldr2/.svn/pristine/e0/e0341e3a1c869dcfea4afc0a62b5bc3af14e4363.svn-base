/*
 * TreeCellCustomRenderer.java
 *
 * Created on 12 avril 2007, 14:40
 *$Id: TreeCellCustomRenderer.java 200 2007-05-26 23:02:02Z zeta $
 */
package ca.qc.bdeb.baldr.ihm.renderers;

import ca.qc.bdeb.baldr.ihm.PanelFiles.FileTreeElement;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * Renderer for the cell in the Jtree
 *
 * @see TreeCellCustomRenderer
 * @author Baldr Team
 */
public class TreeCellCustomRenderer implements TreeCellRenderer {

    /**
     * Icône ajoutée à l'image du dossier s'il est une source.
     */
    private static final Image FLAG_ICON_IMAGE;
    private static final int FLAG_SIZE;

    static {
        ImageIcon icon = new ImageIcon(TreeCellCustomRenderer.class
                .getResource("/Images/sources.png"));
        FLAG_ICON_IMAGE = icon.getImage();
        FLAG_SIZE = icon.getIconHeight();
    }

    /**
     * Creates a new instance of TreeCellCustomRenderer
     */
    private DefaultTreeCellRenderer rend;

    public TreeCellCustomRenderer() {
        rend = new DefaultTreeCellRenderer();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object obj, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel reu;
        if (obj instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
            if (node.getUserObject() instanceof FileTreeElement) { //deals with a file
                reu = (JLabel) rend.getTreeCellRendererComponent(tree, obj, selected, expanded, leaf, row, hasFocus);
                FileTreeElement file = (FileTreeElement) node.getUserObject();
                if (file.isDirectory()) {//if it's a directory, set the icon accordingly
                    Icon dirIcon = (expanded ? rend.getOpenIcon() : rend.getClosedIcon());
                    if (file.isSource()) {
                        dirIcon = addFlagOnIcon();
                    }
                    reu.setIcon(dirIcon);
                } else { //if it's not a directory, it must be a file
                    reu.setIcon(rend.getLeafIcon());
                }
                reu.setText(file.toString());
            } else if (node.getUserObject() instanceof String) { //if it is the root, only string type
                //root is string
                String str = (String) node.getUserObject();
                reu = new JLabel(str);
                if (expanded) {
                    reu.setIcon(rend.getOpenIcon());
                } else {
                    reu.setIcon(rend.getClosedIcon());
                }
            } else {
                throw new IllegalArgumentException("Le type de noeud " + node.getUserObject()
                        .getClass().getCanonicalName() + " n'est pas reconnu.");
            }
        } else {
            throw new IllegalArgumentException("Le noeud n'est pas un DefaultMutableTreeNode.");
        }
        return reu;
    }

    private ImageIcon addFlagOnIcon() {
        Image newImage = new BufferedImage(FLAG_SIZE, FLAG_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.getGraphics();

        g.drawImage(FLAG_ICON_IMAGE, 0, 0, null);
        g.dispose();

        return new ImageIcon(newImage);
    }
}
