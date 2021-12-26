#! /usr/bin/python3
#  -*-coding:Utf-8 -*

#importation des librairies necessaires
import os
import sys
import signal
#utiliser os, sys et signal seulement

#initialisation de variables
label = 0
listePID = []

#[1:] veut dire l'argument 1 et ce qui vient apres
#donc argument [0] est la commande et arg[1:] sera son argument
arguments = sys.argv[1:]


#retourn le pid d'un processus envoye en argument
def findPID(pid):
    for victime in listePID:
        if int(pid) in victime:
            return victime
        return None

#tue tous les processus en foncitonnement
def purge(comm):
    i = 0
    while (i < len(listePID)):
        os.kill(listePID[i][1], signal.SIGKILL)
        i+=1

#quitte le programme
def quitter(comm):
    choix = input("Voulez-vous tuer les processus existants? (o/n)?")
    if(choix == "o"):
        purge(comm)
        sys.exit(0)
    else:
        menu()

#affiche la liste des processus en execution
def liste():
    i = 0
    while (i < len(listePID)):
        
        print(listePID[i])
        i+=1
    
    menu()

#exécute les commandes systemes de base
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

