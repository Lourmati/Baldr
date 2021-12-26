/*
 * Pseudo-licence.
 * */

#define TEST

using System;

/*
Ce programme ne fait pas grand chose. Il sert principalement
à tester le fonctionnement du retrait de commentaires dans le code.
*/

namespace Test {
    public class ClasseTest {
        // La fonction principale.
        public void Main() {
            #ifdef TEST // indique le début du ifdef test.
            Console.WriteLine("Ceci est un exemple de commentaire : // comm");
            Console.WriteLine("Aussi : /* Commentaire! */, oui!");
            Console.WriteLine("Et ici, aucun."); // Aucun commentaire.
            Console.WriteLine(/*"Ancien"*/"Nouveau");
            #endif /* indique la fin du ifdef test */
        }
    }
}

/*
On peut observer ici la fin du programme de csharp
et du même coup, la fin des commentaires
*/