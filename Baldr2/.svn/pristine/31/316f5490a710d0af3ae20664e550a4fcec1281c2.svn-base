/*
 * Savable.java
 *
 * Created on 14 mai 2007, 11:03
 * $Id: Savable.java 200 2007-05-26 23:02:02Z zeta $
 * 
 */
package ca.qc.bdeb.baldr.noyau;

import org.w3c.dom.Node;

/**
 * Interface for saving and restoring Baldr's components
 *
 * @see SaveAndRestore
 * @author zeta
 */
public interface Savable {

    /**
     * Return a stringBuffer containing an XML representation of the component
     * which can be thereafter saved
     *
     * @return a StringBuffer containing XML and reprensenting the component and
     * its "child objects"
     * @see Savable
     */
    public StringBuffer toXml();

    /**
     * Function that reinstate the tab from a DOM object (coming from save)
     *
     * @param node A dom element wich coresponds to the tab
     * @see Savable
     */
    public void fromDom(Node node);

}
