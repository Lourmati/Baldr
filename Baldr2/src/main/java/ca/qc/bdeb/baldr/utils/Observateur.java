package ca.qc.bdeb.baldr.utils;

/**
 * @author Eric Wenaas
 */
public interface Observateur
{
    /**
     */
    public void changementEtat();

    /**
     * @param property La propriété qui a changé
     * @param o La valeur associée à la propriété
     */
    public void changementEtat(Enum<?> property, Object o);
}
