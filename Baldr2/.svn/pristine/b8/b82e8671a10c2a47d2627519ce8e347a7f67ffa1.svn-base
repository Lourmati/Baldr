package ca.qc.bdeb.baldr.formattage;

import ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire;
import ca.qc.bdeb.baldr.formattage.TypeCommentaireMultiLigne.TypeCommentaireJava;
import ca.qc.bdeb.baldr.formattage.TypeCommentaireMultiLigne.TypeCommentaireSql;
import ca.qc.bdeb.baldr.formattage.TypeCommentaireUneLigne.TypeCommentaireAssembleur;
import ca.qc.bdeb.baldr.formattage.TypeCommentaireUneLigne.TypeCommentairePython;
import java.util.LinkedList;

/**
 *
 * @author 1542108
 */
public abstract class TypeCommentaire {

    protected EstCommentaire estUneLigne;
    protected EstCommentaire estMultiLignes;

    public abstract EstCommentaire estCommentaire(LinkedList<Character> etudiesLigne, LinkedList<Character> etudiesMulti);

    public boolean estCommentaireMultiLignesFin(LinkedList<Character> etudiesMultiFin, char caractere) {
        return false;
    }


    public static TypeCommentaire determinerCommentaires(String extension) {
        
        TypeCommentaire type = null;
        
        switch (extension) {
            case "c":
            case "cpp":
            case "cxx":
            case "h":
            case "cs":
            case "java":
                type = new TypeCommentaireJava();
                break;
            case "rh":
            case "py":
            case "sh":
            case "ksh":
            case "bash":
            case "pl":
                type = new TypeCommentairePython();
                break;
            case "lisp":
            case "el":
            case "asm":
                type = new TypeCommentaireAssembleur();
                break;
            case "sql":
                type = new TypeCommentaireSql();
                break;
        }

        return type;
    }
    
    public EstCommentaire getEstUneLigne() {
        return estUneLigne;
    }

    public EstCommentaire getEstMultiLignes() {
        return estMultiLignes;
    }

}
