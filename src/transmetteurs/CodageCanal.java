package transmetteurs;

/**
 * Nom de classe 			: CodageCanal
 * 
 * Description 				: Cette classe se compose des methodes recevoir() et emettre() ainsi que 
 * 							  des methodes de codage du canal de l'information. Elle a pour but de formater l'information numerique
 * 							  en une nouvelle information analogique codee.
 * 
 * Version 					: 1.0
 * 
 * Date 					: 29/09/2021
 * 
 * Copyright 				: Gurvan, Christopher, Alexandre, Aurelien Promotion 2023 FIP 2A
 * 
 */


import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class CodageCanal extends Transmetteur<Boolean,Boolean> {

	private Information<Boolean> informationCodee;
	
	/**
	 * Constructeur de la classe
	 */
	public CodageCanal() {
	}
	
	/**
	 * Methode qui s'occupe de recevoir l'information de la classe utilisee precedemment
	 */
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		this.informationRecue = information;
		this.emettre();
	}

	/** 
	 * 
	 * La methode emettre() de cette classe formate l'information selon la table de correspondance associee.
	 * Elle instancie un attribut <informationCodee> qui sera envoye au prochain element de la chaine
	 * de transmission.
	 * 
	 */
	public void emettre() throws InformationNonConformeException {
		
		informationCodee = new Information<Boolean>();
		
		// on ajoute soit 010 soit 101 suivant le bit qui est reçu (CDC)
		for(Boolean bitCurrent : informationRecue) {
			if (bitCurrent) {
				informationCodee.add(true);
				informationCodee.add(false);
				informationCodee.add(true);
			}else {
				informationCodee.add(false);
				informationCodee.add(true);
				informationCodee.add(false);
			}
		}
		// puis on envoie l'information codee
		for ( DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationCodee);
		}
		this.informationEmise = informationCodee;	
	}
}
