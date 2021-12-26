/*
 * transferable.java
 *
 * Created on 23 mai 2007, 21:05
 */
package ca.qc.bdeb.baldr.ihm;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.tree.DefaultMutableTreeNode;

public class TransferableTreeNode extends DefaultMutableTreeNode implements Transferable {

    private final static int TREE = 0;

    final public static DataFlavor DEFAULT_MUTABLE_TREENODE_FLAVOR = new DataFlavor(
            DefaultMutableTreeNode.class, "Default Mutable Tree Node");

    static DataFlavor flavors[] = {DEFAULT_MUTABLE_TREENODE_FLAVOR, DataFlavor.javaFileListFlavor};

    private DefaultMutableTreeNode data;

    public TransferableTreeNode(DefaultMutableTreeNode data) {
        this.data = data;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        Object returnObject;
        if (flavor.equals(flavors[TREE])) {
            Object userObject = data.getUserObject();
            if (userObject == null) {
                returnObject = data;
            } else {
                returnObject = userObject;
            }
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
        return returnObject;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {

        for (int i = 0, n = flavors.length; i < n; i++) {
            if (flavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
}
