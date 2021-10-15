package sources;

import information.*;
import destinations.DestinationInterface;
import java.util.*;

/** 
 * Classe Abstraite d'un composant source d'informations dont les
 * elements sont de type T
 * @author prou
 */
public  abstract class Source <T> implements  SourceInterface <T> {
   
    /** 
     * la liste des composants destination connectes
     */
    protected LinkedList <DestinationInterface <T>> destinationsConnectees;
   
    /** 
     * l'information generee par la source
     */
    protected Information <T>  informationGeneree;
   	
    /** 
     * l'information emise par la source
     */
    protected Information <T>  informationEmise;
   	
    /** 
     * un constructeur factorisant les initialisations communes aux
     * realisations de la classe abstraite Source
     */
    public Source () {
	destinationsConnectees = new LinkedList <DestinationInterface <T>> ();
	informationGeneree = null;
	informationEmise = null;
    }
    
    /**
     * retourne la derniere information emise par la source
     * @return une information   
     */
    public Information <T>  getInformationEmise() {
	return this.informationEmise;
    }
   
    /**
     * connecte une destination a la source
     * @param destination  la destination a connecter
     */
    public void connecter (DestinationInterface <T> destination) {
	destinationsConnectees.add(destination); 
    }
   
    /**
     * deconnecte une destination de la source
     * @param destination  la destination a deconnecter
     */
    public void deconnecter (DestinationInterface <T> destination) {
	destinationsConnectees.remove(destination); 
    }
   
    /**
     * emet l'information generee
     */
    public void emettre() throws InformationNonConformeException {
       	// emission vers les composants connectes
	for (DestinationInterface <T> destinationConnectee : destinationsConnectees) {
            destinationConnectee.recevoir(informationGeneree);
	}
	this.informationEmise = informationGeneree;   			 			      
    }
}
