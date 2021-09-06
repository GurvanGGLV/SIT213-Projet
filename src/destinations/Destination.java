package destinations;

import information.*;

/** 
 * Classe Abstraite d'un composant destination d'informations dont les
 * éléments sont de type T
 * @author prou
 */
public  abstract class Destination <T> implements DestinationInterface <T> {
    
    /** 
     * l'information reçue par la destination
     */
    protected Information <T>  informationRecue;
    
    /** 
     * un constructeur factorisant les initialisations communes aux
     * réalisations de la classe abstraite Destination
     */
    public Destination() {
	informationRecue = null;
    }

    /**
     * retourne la dernière information reçue par la destination
     * @return une information   
     */
    public Information  <T>  getInformationRecue() {
	return this.informationRecue;
    }
   	    
    /**
     * reçoit une information
     * @param information  l'information  à recevoir
     */
    public  abstract void recevoir(Information <T> information) throws InformationNonConformeException;  
}
