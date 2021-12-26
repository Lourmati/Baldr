
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * La classe principale du jeu Plus petit-plus grand, où il faut deviner
 * un nombre déterminé aléatoirement pour remporter la partie.
 * @author Pascal Laprade
 */
public class PasCopie {
    
    enum Resultat {
        PLUS_PETIT,
        PLUS_GRAND,
        EGAL
    }
    
    private static BufferedReader stdin;
    
    private static int nombreMystere;
    
    public static void main(String[] args) {
        Random rand = new Random();
        
        nombreMystere = rand.nextInt(100);
        
        stdin = new BufferedReader(new InputStreamReader(System.in));
        
        Resultat res;
        
        do {
            System.out.print("Devine le nombre mystère (0 à 100) : ");
            
            int nombre = lireNombre();
            
            res = verifierReponse(nombre);
            
            switch(res) {
                case EGAL:
                    System.out.println("Vous avez gagné!");
                    break;
                    
                case PLUS_PETIT:
                    System.out.println("Plus petit.");
                    break;
                    
                case PLUS_GRAND:
                    System.out.println("Plus grand.");
                    break;
            }
        } while(res != Resultat.EGAL);
    }
    
    private static int lireNombre() {
        int nombre = -1;
        
        try {
            String reponse = stdin.readLine();
            nombre = Integer.parseInt(reponse);
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
        
        return nombre;
    }
    
    private static Resultat verifierReponse(int nombre) {
        Resultat res;
        
        if(nombreMystere < nombre) {
            res = Resultat.PLUS_PETIT;
        } else if(nombreMystere > nombre) {
            res = Resultat.PLUS_GRAND;
        } else {
            res = Resultat.EGAL;
        }
        
        return res;
    }
}
