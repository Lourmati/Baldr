
import os
import sys
import signal

label = 0
listePID = []

arguments = sys.argv[1:]


def findPID(pid):
    for victime in listePID:
        if int(pid) in victime:
            return victime
        return None

def purge(comm):
    i = 0
    while (i < len(listePID)):
        os.kill(listePID[i][1], signal.SIGKILL)
        i+=1

def quitter(comm):
    choix = input("Voulez-vous tuer les processus existants? (o/n)?")
    if(choix == "o"):
        purge(comm)
        sys.exit(0)
    else:
        menu()

def liste():
    i = 0
    while (i < len(listePID)):
        
        print(listePID[i])
        i+=1
    
    menu()

def commande(label):
    arg = label.split()
    commB = arg[0]
    
    tube = os.pipe()
    pid = os.fork()

    listePID.append((commB, pid))

    if pid == 0:
        os.close(tube[0])
        os.dup2(tube[1], 2)
        
        os.execvp(commB, arg)

        sys.exit(1)
    elif pid > 0:
        os.close(tube[1])

        print("le programme {0} a ete execute avec le pid {1}".format(commB, pid))
    else:
        print("Impossible de creer le processus", file=sys.stderr)
        sys.exit(1)
        
    menu()

