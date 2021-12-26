/*
 * Pseudo-licence. (different)
 * */

#define TEST

using System;

/*
Ce programme ne fait pas grand chose. Il sert principalement (different)
à tester le fonctionnement du retrait de commentaires dans le code. (different)
*/

namespace Test {
    public class ClasseTest {
        // La fonction principale. (different)
        public void Main() {
            #ifdef TEST // indique le début du ifdef test. (different)
            Console.WriteLine("Ceci est un exemple de commentaire : // comm");
            Console.WriteLine("Aussi : /* Commentaire! */, oui!");
            Console.WriteLine("Et ici, aucun."); // Aucun commentaire. (different)
            Console.WriteLine(/*"Ancien (different)"*/"Nouveau");
            #endif /* indique la fin du ifdef test (different) */
        }
    }
}

/*
On peut observer ici la fin du programme de csharp (different)
et du même coup, la fin des commentaires (different)
*/