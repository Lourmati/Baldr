/*
 * BaldrFileFilter.java
 *
 * Created on 19 mai 2007, 18:01
 *$Id: BaldrFileFilter.java 242 2007-08-21 19:43:06Z cedric $
 */
package ca.qc.bdeb.baldr.ihm.fileFilters;

import ca.qc.bdeb.baldr.utils.Extension;

import java.io.File;
import java.util.ResourceBundle;
import javax.swing.filechooser.FileFilter;

/**
 * Class describing the filefilter which accepts uncompressed baldr files
 *
 * @author zeta
 */
public class BaldrFileFilter extends FileFilter {

    private ResourceBundle messages;

    /**
     * Creates a new instance of BaldrFileFilter
     */
    public BaldrFileFilter() {
        messages = ResourceBundle.getBundle("i18n/Baldr");
    }

    /**
     * Whether the file is acceptable
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String ext = Extension.getExtension(f);
        return ext != null && ext.equalsIgnoreCase(Extension.BALDR);
    }

    /**
     * Description for the filechooser
     */
    @Override
    public String getDescription() {
        return messages.getString("FT_BALDR") + " (." + Extension.BALDR + ")";
    }

}
