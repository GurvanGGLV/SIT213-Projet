package destinations;
//test
import information.*;

/** 
 * Classe Abstraite d'un composant destination d'informations dont les
 * elements sont de type T
 * @author prou
 */
public  abstract class Destination <T> implements DestinationInterface <T> {
    
    /** 
     * l'information recue par la destination
     */
    protected Information <T>  informationRecue;
    
    /** 
     * un constructeur factorisant les initialisations communes aux
     * realisations de la classe abstraite Destination
     */
    public Destination() {
	informationRecue = null;
    }

    /**
     * retourne la derniere information recue par la destination
     * @return une information   
     */
    public Information  <T>  getInformationRecue() {
	return this.informationRecue;
    }
   	    
    /**
     * recoit une information
     * @param information  l'information a recevoir
     */
    public  abstract void recevoir(Information <T> information) throws InformationNonConformeException;  
}
