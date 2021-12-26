
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Le jeu où il faut deviner un nombre mystère.
 * @author Pascal Laprade
 */
public class CopiePlusSubtile {
    /**
     * Le main.
     * @param args
     */
    public static void main(String[] args) {
        int reponse = new Random().nextInt(100);
        
        String evaluation;
        int nombre;
        do {
            demanderNombre();
            nombre = lireNombre();
            evaluation = comparerNombre(nombre, reponse);
            System.out.println(evaluation);
        }
        while(evaluation.equals("Non, plus grand!") ||
                evaluation.equals("Non, plus petit!"));
    }
    
    /**
     * Demander d'entrer un nombre.
     */
    private static void demanderNombre() {
        System.out.print("Entrez un nombre (0 à 100) : ");
    }
    
    /**
     * Obtenir le nombre.
     * @return La réponse du joueur
     */
    private static int lireNombre() {
        int leNombre = 0;
        
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(System.in));
        
        try {
            leNombre = Integer.parseInt(bufferedReader.readLine());
        } catch(Exception exception) {
        }
        
        return leNombre;
    }
    
    /**
     * Vérifier si le nombre est plus petit ou plus grand.
     * @param reponseJoueur Le nombre que le joueur a donné
     * @param nombre Le bon nombre
     * @return si le nombre est plus petit ou plus grand
     */
    private static String comparerNombre(int reponseJoueur, int nombre) {
        String retour = "";
        
        if(nombre < reponseJoueur) {
            retour = "Non, plus petit!";
        } else if(nombre > reponseJoueur) {
            retour = "Non, plus grand!";
        } else {
            retour = "Bravo, vous avez trouvé le bon nombre!";
        }
        
        return retour;
    }
}