package ca.qc.bdeb.baldr.formattage;

import ca.qc.bdeb.baldr.formattage.CommentParser.*;
import static ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire.NON;
import static ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire.OUI;
import java.util.LinkedList;

/**
 *
 * @author 1542108
 */
public class TypeCommentaireUneLigne extends TypeCommentaire {

    private final char commentaireUneLigne;

    public TypeCommentaireUneLigne(char commentaireUneLigne) {
        this.commentaireUneLigne = commentaireUneLigne;
    }

    @Override
    public EstCommentaire estCommentaire(LinkedList<Character> etudiesLigne, LinkedList<Character> etudiesMulti) {

        if (etudiesLigne.size() > 1) {
            etudiesLigne.removeFirst();
        }

        if (etudiesLigne.getFirst() == commentaireUneLigne) {
            estUneLigne = OUI;
            return estUneLigne;
        }
        return NON;
    }

    public static class TypeCommentairePython extends TypeCommentaireUneLigne {

        public TypeCommentairePython() {
            super('#');
        }

    }

    public static class TypeCommentaireAssembleur extends TypeCommentaireUneLigne {

        public TypeCommentaireAssembleur() {
            super(';');
        }

    }

}
