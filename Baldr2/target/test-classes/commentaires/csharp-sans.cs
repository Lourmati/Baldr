

#define TEST

using System;



namespace Test {
    public class ClasseTest {
        
        public void Main() {
            #ifdef TEST
            Console.WriteLine("Ceci est un exemple de commentaire : // comm");
            Console.WriteLine("Aussi : /* Commentaire! */, oui!");
            Console.WriteLine("Et ici, aucun."); 
            Console.WriteLine("Nouveau");
            #endif 
        }
    }
}
