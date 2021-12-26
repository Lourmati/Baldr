
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Le jeu du plus petit-plus grand.
 * @author Pascal Laprade
 */
public class PureCopie {
    /**
     * La fonction principale du jeu.
     * @param args Les arguments passés au programme
     */
    public static void main(String[] args) {
        int nombreMystere = new Random().nextInt(100);
        
        String reponse;
        do {
            demanderNombreUtilisateur();
            int nombre = obtenirNombre();
            reponse = verifierNombre(nombre, nombreMystere);
            System.out.println(reponse);
        } while(reponse.equals("Plus grand!") || reponse.equals("Plus petit!"));
    }
    
    /**
     * Demande un nombre à l'utilisateur.
     */
    private static void demanderNombreUtilisateur() {
        System.out.print("Entrez un nombre de 0 à 100 : ");
    }
    
    /**
     * Obtient un nombre de l'utilisateur.
     * @return Le nombre
     */
    private static int obtenirNombre() {
        int nombre = 0;
        
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            nombre = Integer.parseInt(r.readLine());
        } catch(Exception e) {
        }
        
        return nombre;
    }
    
    /**
     * Vérifie le résultat du nombre.
     * @param nombre Le nombre à vérifier
     * @param bonneReponse La bonne réponse
     * @return Le résultat du nombre
     */
    private static String verifierNombre(int nombre, int bonneReponse) {
        String retour = "";
        
        if(bonneReponse < nombre) {
            retour = "Plus petit!";
        } else if(bonneReponse > nombre) {
            retour = "Plus grand!";
        } else {
            retour = "Vous avez gagné! Félicitations!";
        }
        
        return retour;
    }
}