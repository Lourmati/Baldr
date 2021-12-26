package ca.qc.bdeb.baldr.utils;

/**
 * 
 * @author Eric Wenaas
 * 
 */

public interface Observable
{

    /**
     * @param ob the observer
     */
    public void ajouterObservateur(Observateur ob);

    /**
     * @param ob the observer
     */
    public void retirerObservateur(Observateur ob);

    /**
     */
    public void aviserObservateurs();

    /**
     * @param propriete La propriété modifiée
     * @param o Valeur associée a la propriété
     */
    public void aviserObservateurs(Enum<?> propriete, Object o);

}
