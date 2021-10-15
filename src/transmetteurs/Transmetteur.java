package transmetteurs;

import sources.*;
import destinations.*;
import information.*;

import java.util.*;

/** 
 * Classe Abstraite d'un composant transmetteur d'informations dont
 * les elements sont de type R en entree et de type E en sortie;
 * l'entree du transmetteur implemente l'interface
 * DestinationInterface, la sortie du transmetteur implemente
 * l'interface SourceInterface
 * @author prou
 */
public abstract  class Transmetteur <R,E> implements  DestinationInterface <R>, SourceInterface <E> {
   
    /** 
     * la liste des composants destination connectes en sortie du transmetteur 
     */
    protected LinkedList <DestinationInterface <E>> destinationsConnectees;
   
    /** 
     * l'information reçue en entree du transmetteur 
     */
    protected Information <R>  informationRecue;
		
    /** 
     * l'information emise en sortie du transmetteur
     */		
    protected Information <E>  informationEmise;
   
    /** 
     * un constructeur factorisant les initialisations communes aux
     * realisations de la classe abstraite Transmetteur
     */
    public Transmetteur() {
	destinationsConnectees = new LinkedList <DestinationInterface <E>> ();
	informationRecue = null;
	informationEmise = null;
    }
   	
    /**
     * retourne la derniere information reçue en entree du
     * transmetteur
     * @return une information   
     */
    public Information <R>  getInformationRecue() {
	return this.informationRecue;
    }

    /**
     * retourne la derniere information emise en sortie du
     * transmetteur
     * @return une information   
     */
    public Information <E>  getInformationEmise() {
	return this.informationEmise;
    }

    /**
     * connecte une destination a la sortie du transmetteur
     * @param destination  la destination a connecter
     */
    public void connecter (DestinationInterface <E> destination) {
	destinationsConnectees.add(destination); 
    }

    /**
     * deconnecte une destination de la la sortie du transmetteur
     * @param destination  la destination a deconnecter
     */
    public void deconnecter (DestinationInterface <E> destination) {
	destinationsConnectees.remove(destination); 
    }
   	    
    /**
     * reçoit une information.  Cette methode, en fin d'execution,
     * appelle la methode emettre.
     * @param information  l'information  reçue
     */
    public  abstract void recevoir(Information <R> information) throws InformationNonConformeException;
   
    /**
     * emet l'information construite par le transmetteur
     */
    public  abstract void emettre() throws InformationNonConformeException;   
}
