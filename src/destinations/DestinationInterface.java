package destinations;

import information.*;


/** 
 * Interface d'un composant ayant le comportement d'une destination
 * d'informations dont les elements sont de type T
 * @author prou
 */
public  interface DestinationInterface <T>  {   
   
    /**
     * pour obtenir la derniere information recue par une destination.
     * @return une information   
     */  
    public Information <T>  getInformationRecue(); 
   	 
    /**
     * pour recevoir une information de la source qui nous est
     * connectee
     * @param information  l'information a recevoir
     */
    public void recevoir(Information <T> information) throws InformationNonConformeException;
   
}
