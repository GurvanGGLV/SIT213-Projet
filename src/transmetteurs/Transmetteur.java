package transmetteurs;

import sources.*;
import destinations.*;
import information.*;

import java.util.*;

/** 
 * Classe Abstraite d'un composant transmetteur d'informations dont
 * les éléments sont de type R en entrée et de type E en sortie;
 * l'entrée du transmetteur implémente l'interface
 * DestinationInterface, la sortie du transmetteur implémente
 * l'interface SourceInterface
 * @author prou
 */
public abstract  class Transmetteur <R,E> implements  DestinationInterface <R>, SourceInterface <E> {
   
    /** 
     * la liste des composants destination connectés en sortie du transmetteur 
     */
    protected LinkedList <DestinationInterface <E>> destinationsConnectees;
   
    /** 
     * l'information reçue en entrée du transmetteur 
     */
    protected Information <R>  informationRecue;
		
    /** 
     * l'information émise en sortie du transmetteur
     */		
    protected Information <E>  informationEmise;
   
    /** 
     * un constructeur factorisant les initialisations communes aux
     * réalisations de la classe abstraite Transmetteur
     */
    public Transmetteur() {
	destinationsConnectees = new LinkedList <DestinationInterface <E>> ();
	informationRecue = null;
	informationEmise = null;
    }
   	
    /**
     * retourne la dernière information reçue en entrée du
     * transmetteur
     * @return une information   
     */
    public Information <R>  getInformationRecue() {
	return this.informationRecue;
    }

    /**
     * retourne la dernière information émise en sortie du
     * transmetteur
     * @return une information   
     */
    public Information <E>  getInformationEmise() {
	return this.informationEmise;
    }

    /**
     * connecte une destination à la sortie du transmetteur
     * @param destination  la destination à connecter
     */
    public void connecter (DestinationInterface <E> destination) {
	destinationsConnectees.add(destination); 
    }

    /**
     * déconnecte une destination de la la sortie du transmetteur
     * @param destination  la destination à déconnecter
     */
    public void deconnecter (DestinationInterface <E> destination) {
	destinationsConnectees.remove(destination); 
    }
   	    
    /**
     * reçoit une information.  Cette méthode, en fin d'exécution,
     * appelle la méthode émettre.
     * @param information  l'information  reçue
     */
    public  abstract void recevoir(Information <R> information) throws InformationNonConformeException;
   
    /**
     * émet l'information construite par le transmetteur
     */
    public  abstract void emettre() throws InformationNonConformeException;   
}
