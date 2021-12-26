package ca.qc.bdeb.baldr.noyau;

/**
 * Représente une matrice triangulaire (matrice dont la diagonale est nulle).
 *
 * @author Équipe de dev. Baldr
 */
public class MatriceTriangulaire implements Cloneable {

    /**
     * Le contenu de la matrice : les résultats des comparaisons.
     */
    private float[][] res;

    /**
     * Le nombre d'itérations.
     */
    private int nbIterations;

    // TODO(pascal) : Déterminer ce que représente cette variable :
    // un numéro d'analyse (dans quel contexte l'utilise-t-on alors?)
    // ou le nombre d'analyses contenues dans la matrice? Ou autre chose?
    private int numAnalyse;

    /**
     * Créée une nouvelle instance de MatriceTriangulaire, avec un nombre de
     * fichiers défini.
     *
     * @param nbFichier Le nombre de fichiers contenus dans l'analyse.
     */
    public MatriceTriangulaire(int nbFichier) {
        res = new float[nbFichier][];

        for (int i = 0; i < nbFichier; i++) {
            res[i] = new float[i + 1];

            for (int j = 0; j < i + 1; j++) {
                res[i][j] = -1;

                if (i == j) {
                    res[i][j] = 0;
                } else {
                    this.numAnalyse++;
                }
            }
        }

        this.nbIterations = calculerNbIterations();
    }
    
    @Override
    public MatriceTriangulaire clone() throws CloneNotSupportedException {
        super.clone();
        
        MatriceTriangulaire copie = new MatriceTriangulaire(res.length);
        
        copie.res = res.clone();
        copie.nbIterations = nbIterations;
        copie.numAnalyse = numAnalyse;
        
        return copie;
    }

    /**
     * Échange des valeurs de place dans la matrice.
     *
     * @param i La rangée de la matrice.
     * @param j La colonne de la matrice.
     */
    public void echangerValeurs(int i, int j) {
        int x = 0;
        int y = 0;

        if (i > j) {
            int temp = i;
            i = j;
            j = temp;
        }

        for (int iterateur = 0; iterateur < nbIterations; iterateur++) {
            if (x == i) {
                if (y == i) {
                    float temporaire = res[i][i];

                    setRes(i, i, res[j][j]);
                    setRes(j, j, temporaire);
                } else {
                    float temporaire = res[i][y];

                    setRes(i, y, res[j][y]);
                    setRes(j, y, temporaire);
                }
            } else if (x > i && x < j) {
                if (y == i) {
                    float temporaire = res[x][i];

                    setRes(x, i, res[j][x]);
                    setRes(j, x, temporaire);
                }
            } else if (x > j) {
                if (y == i) {
                    float temporaire = res[x][i];

                    setRes(x, i, res[x][j]);
                    setRes(x, j, temporaire);
                }
            }

            if (x == y) {
                y = 0;
                x++;
            } else {
                y++;
            }
        }
    }

    /**
     * Recrée la matrice en enlevant la ligne [indice] et la colonne [indice].
     *
     * @param indice Indice de la ligne et de la colonne à enlever.
     */
    public void enleverLigneEtColonne(int indice) {
        float[][] newRes = new float[res.length - 1][];

        numAnalyse = 0;

        for (int i = 0, newi = 0; i < res.length; i++) {
            if (i != indice) {
                newRes[newi] = new float[newi + 1];

                for (int j = 0, newj = 0; j < res[i].length; j++) {
                    if (j != indice) {
                        newRes[newi][newj] = res[i][j];
                        newj++;
                        if (newi != newj) {
                            numAnalyse++;
                        }
                    }
                }
                newi++;
            }
        }

        res = newRes;
        nbIterations = calculerNbIterations();
      
    }

    /**
     * Retourne le nombre d'itérations nécessaires pour parcourir toute la
     * matrice.
     *
     * @return Le nombre d'itérations pour parcourir le tableau.
     */
    private int calculerNbIterations() {
        int somme = 0;

        for (int i = 0; i <= res.length; i++) {
            somme += i;
        }

        return somme;
    }

    /**
     * Permet d'assigner le résultat dans une cellule de la matrice.
     *
     * @param i La coordonnée de la rangée de la cellule.
     * @param j La coordonnée de la colonne de la cellule.
     * @param val La valeur à insérer dans la cellule.
     */
    public void setRes(int i, int j, float val) {
        if (res[i].length > j) {
            res[i][j] = val;
        } else {
            res[j][i] = val;
        }
    }

    /**
     * Retourne le plus petit résultat d'une rangée donnée.
     *
     * @param i La coordonnée de la rangée dans laquelle chercher.
     * @return Le plus petit résultat de la rangée.
     */
    public float getMinRes(int i) {
        float minRes = -1;

        for (int j = 0; j < res.length; j++) {
            if (i > j
                    && (minRes == -1 || res[i][j] < minRes)
                    && res[i][j] != 0) {
                minRes = res[i][j];
            } else if (i < j
                    && (minRes == -1 || res[j][i] < minRes)
                    && res[j][i] != 0) {
                minRes = res[j][i];
            }
        }

        return minRes;
    }

    /**
     * Retourne le résultat contenu à une cellule donnée.
     *
     * @param i La coordonnée de la rangée de la cellule.
     * @param j La coordonnée de la colonne de la cellule.
     * @return Le résultat contenu à cette cellule.
     */
    public float getResAt(int i, int j) {
        return res[i][j];
    }

    // TODO(pascal) : Écrire la Javadoc lorsque sera trouvée la signification
    // de "numAnalyse".
    public int getNumAnalyse() {
        return numAnalyse;
    }

    /**
     * Retourne le résultat contenu à une cellule donnée de la matrice.
     *
     * @param i La coordonnée de la rangée de la cellule.
     * @param j La coordonnée de la colonne de la cellule.
     * @return Le résultat contenu à la cellule.
     */
    public float getRes(int i, int j) {
        if (res[i].length > j) {
            return res[i][j];
        } else {
            return res[j][i];
        }

    }

    // TODO(pascal) : Trouver ce que fait exactement la méthode pour
    // pouvoir rédiger la Javadoc correspondante.
    public int compterAnalyse() {
        int compteur = 0;

        int x = 0;
        int y = 0;

        for (int i = 0; i < nbIterations; i++) {

            if (res[x][y] == -1) {
                compteur++;
            }

            if (x == y) {
                y = 0;
                x++;
            } else {
                y++;
            }
        }

        return compteur;
    }

    /**
     * Permet d'accéder à toutes les valeurs de la matrice.
     *
     * @return Les valeurs contenues dans la matrice.
     */
    public float[][] getValues() {
        return res;
    }

    /**
     * Permet d'accéder à la longueur totale de la matrice.
     *
     * @return La longueur totale de la matrice.
     */
    public int getLength() {
        return res.length;
    }

    /**
     * Retourne la plus petite et la plus grande valeurs, en excluant les zéros.
     *
     * @return Les deux éléments trouvés, dans un tableau.
     */
    public float[] getMinMaxValues() {
        // Initialisées pour que le compilateur soit content.
        float min = 0, max = 0;

        // A trouvé des valeurs != 0 ?
        boolean valsTrouvees = false;

        for (float[] ligne : res) {
            for (float val : ligne) {
                if (val != 0) {
                    if (!valsTrouvees) {
                        min = max = val;
                        valsTrouvees = true;
                    } else {
                        if (val < min) {
                            min = val;
                        } else if (val > max) {
                            max = val;
                        }
                    }
                }
            }
        }

        if (valsTrouvees) {
            return new float[]{min, max};
        } else {
            return new float[]{0, 0};
        }
    }
}
