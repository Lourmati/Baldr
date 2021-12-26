package ca.qc.bdeb.baldr.formattage;

import ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire;
import static ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire.NON;
import static ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire.OUI;
import static ca.qc.bdeb.baldr.formattage.CommentParser.EstCommentaire.PREMIER_CARACTERE;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 1542108
 */
public abstract class TypeCommentaireMultiLigne extends TypeCommentaire {

    private final char[] commentaireMultiOuvrant;
    private final char[] commentaireMultiFermant;
    private final char[] commentaireUneLigne;

    public TypeCommentaireMultiLigne(char[] commentaireMultiOuvrant,
            char[] commentaireMultiFermant, char[] commentaireUneLigne) {
        this.commentaireMultiOuvrant = commentaireMultiOuvrant;
        this.commentaireMultiFermant = commentaireMultiFermant;
        this.commentaireUneLigne = commentaireUneLigne;
    }

    @Override
    public EstCommentaire estCommentaire(LinkedList<Character> etudiesLigne, LinkedList<Character> etudiesMulti) {
        estUneLigne = estCommentaireLigne(etudiesLigne, commentaireUneLigne);
        estMultiLignes = estCommentaireLigne(etudiesMulti, commentaireMultiOuvrant);

        if (estUneLigne == OUI || estMultiLignes == OUI) {
            return OUI;
        } else if ((estUneLigne == PREMIER_CARACTERE || estMultiLignes == PREMIER_CARACTERE)) {
            return PREMIER_CARACTERE;
        } else {
            return NON;
        }
    }

    private EstCommentaire estCommentaireLigne(LinkedList<Character> etudiesLigne, char[] charCommentaire) {

        if (etudiesLigne.size() > charCommentaire.length) {
            etudiesLigne.removeFirst();
        }

        if (comparerCaracteres(etudiesLigne, charCommentaire)) {
            return OUI;
        } else if (etudiesLigne.getFirst() == charCommentaire[0]) {
            return PREMIER_CARACTERE;
        } else {
            return NON;
        }
    }

    private boolean comparerCaracteres(List<Character> listeChars, char[] tabChars) {
        if (listeChars.size() != tabChars.length) {
            return false;
        }

        for (int i = 0; i < listeChars.size(); i++) {
            if (listeChars.get(i) != tabChars[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean estCommentaireMultiLignesFin(LinkedList<Character> etudiesMultiFin, char caractere) {
        etudiesMultiFin.add(caractere);

        if (etudiesMultiFin.size() > commentaireMultiFermant.length) {
            etudiesMultiFin.removeFirst();
        }

        return comparerCaracteres(etudiesMultiFin, commentaireMultiFermant);
    }

    public static class TypeCommentaireJava extends TypeCommentaireMultiLigne {

        public TypeCommentaireJava() {
            super(new char[]{'/', '*'}, new char[]{'*', '/'}, new char[]{'/', '/'});
        }

    }

    public static class TypeCommentaireSql extends TypeCommentaireMultiLigne {

        public TypeCommentaireSql() {
            super(new char[]{'/', '*'}, new char[]{'*', '/'}, new char[]{'-', '-'});
        }

    }

}
