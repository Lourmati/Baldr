/*
 * OutputStreamSizer.java
 *
 * Created on 26 mai 2007, 10:46
 *$Id: OutputStreamSizer.java 200 2007-05-26 23:02:02Z zeta $
 */
package ca.qc.bdeb.baldr.noyau;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Simule un flux, mais ne fait qu'en calculer la taille (n'écrit rien).
 *
 * @author zeta
 */
public class OutputStreamSizer extends OutputStream {

    private long size;

    /**
     * Créée une nouvelle instance de OutputStreamSizer.
     */
    public OutputStreamSizer() {
        size = 0;
    }

    /**
     * Écrit un caractère dans le compteur de caractères.
     *
     * @param b Un byte à écrire comme int
     * @throws IOException
     */
    @Override
    public void write(int b) throws IOException {
        size++;
    }

    /**
     * Retourne la taille lue.
     *
     * @return La taille lue depuis le début du flux
     */
    public long getSize() {
        return size;
    }
}
