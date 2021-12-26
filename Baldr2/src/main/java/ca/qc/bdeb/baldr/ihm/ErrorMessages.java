/*
 * Error.java
 *
 * Created on 14 avril 2007, 18:42
 *$Id: Error.java 241 2007-08-21 18:20:38Z cedric $
 */
package ca.qc.bdeb.baldr.ihm;

import javax.swing.JOptionPane;
import java.util.ResourceBundle;

/**
 * Class handling the different type of errors and their diplay to the users
 *
 * @author zeta
 */
public class ErrorMessages {

    private static final ResourceBundle messages = ResourceBundle.getBundle("i18n/Baldr");

    private static void displayError(String message) {
        JOptionPane.showMessageDialog(null, message, messages.getString("ERROR"), JOptionPane.ERROR_MESSAGE);
    }

    public static void tropAnalyse() {
        displayError(messages.getString("TOO_MUCH_ANALYSIS"));
    }

    public static void noFiles() {
        displayError(messages.getString("NO_FILES"));
    }

    public static void notEnoughFiles() {
        displayError(messages.getString("NO_ENOUGH_FILES"));
    }
    
    public static void noHiddenFiles() {
        displayError(messages.getString("NO_HIDDEN_FILES"));
    }

    public static void noEditorDefined() {
        displayError(messages.getString("NO_EDITOR"));
    }

    public static void noComparatorDefined() {
        displayError(messages.getString("NO_DIFF"));
    }

    public static void nothingToSave() {
        displayError(messages.getString("NOTHING_SAVE"));
    }

    public static void cannotExecute() {
        displayError(messages.getString("CANNOT_EXECUTE_PROGRAM"));
    }
    
    public static void invalidFile() {
        displayError(messages.getString("INVALID_FILE"));
    }
}
