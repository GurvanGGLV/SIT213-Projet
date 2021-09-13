package sources;

import information.*;
import destinations.DestinationInterface;

/** 
 * Interface d'un composant ayant le comportement d'une source
 * d'informations dont les éléments sont de type T
 * @author prou
 */
public interface SourceInterface <T>  {
   
    /**
     * pour obtenir la dernière information émise par une source.
     * @return une information   
     */
    public Information <T>  getInformationEmise();
   
    /**
     * pour connecter une destination à la source
     * @param destination  la destination à connecter
     */
    public void connecter (DestinationInterface <T> destination);
   
    /**
     * pour émettre l'information contenue dans une source
     */
    public void emettre() throws InformationNonConformeException; 
}
