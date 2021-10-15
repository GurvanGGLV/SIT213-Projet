package transmetteurs;

/**
 * Nom de classe 			: DecodageCanal
 * 
 * Description 				: Cette classe se compose des methodes recevoir() et emettre() ainsi que 
 * 							  d'une methode de decodage de l'information. Elle a pour but deformater l'information numerique
 * 							  codee en une information analogiqu.
 * 
 * Version 					: 1.0
 * 
 * Date 					: 19/09/2021
 * 
 * Copyright 				: Gurvan, Christopher, Alexandre, Aurelien Promotion 2023 FIP 2A
 * 
 */

import java.util.Iterator;

import destinations.DestinationInterface;

import java.util.Arrays;

import information.Information;
import information.InformationNonConformeException;

public class DecodageCanal extends Transmetteur<Boolean,Boolean> {
	
	// liste qui va contenir le signal decode
	protected Information<Boolean> informationDecodee;
	
	/**
	 * Constructeur de la classe qui initialise la liste informationDecodee
	 */
	public DecodageCanal() {
		informationDecodee = null;
	}

	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		this.informationRecue = information;
		this.emettre();
	}

	/** 
	 * La methode emettre() de cette classe deformate l'information numerique apres decodage de celle ci par 
	 * le transmetteur adapte.
	 * Elle transforme ainsi cette information en l'information numerique utile.
	 * L'attribut <informationDecodee> est instancie pour stocker les valeurs correspondantes des bits.
	 * Elle sera ensuite envoyee a l'element destination de la chaine de transmission.
	 * 
	 */
	public void emettre() throws InformationNonConformeException {
		
		informationDecodee = new Information<Boolean>();
		
		// la methode has.next() permet de parcourir la trame d'information numerique.
		
		for(Iterator<Boolean> iterator = informationRecue.iterator() ; iterator.hasNext();) 
		{
			
			Boolean bit1 = iterator.next(); // etude sur le premier bit 
			Boolean bit2 = iterator.next();	// etude sur le second bit
			Boolean bit3 = iterator.next(); // etude sur le troisieme bit 
			
			Boolean[] tabBit = {bit1 , bit2 , bit3};
			//System.out.println(bit1 + " " + bit2 + " " + bit3);
			
			// sachant qu'on a les 3 bits voulus, on les convertit en string pour les comparer
			
			String tabBitString = Arrays.toString(tabBit);
			
			// la structure "switch case" nous permettra d'effectuer la correspondance entre les
			// valeurs de <tabBitString> et les parametres conditionnels.
			// Selon la correspondance des trois bits et les parametres conditionnels, on ajoute
			// a <tabBitString> la valeur du bit associe, 0 ou 1.
			
			switch(tabBitString) {
			case  "[true, false, true]":
				informationDecodee.add(true);
				break;
			
			case "[true, false, false]":
				informationDecodee.add(true);
				break;
			
			case "[false, false, false]":
				informationDecodee.add(false);
				break;
				
			case "[false, false, true]":
				informationDecodee.add(true);
				break;
				
			case "[false, true, false]":
				informationDecodee.add(false);
				break;
				
			case "[false, true, true]":
				informationDecodee.add(false);
				break;
				
			case "[true, true, false]":
				informationDecodee.add(false);
				break;

			case "[true, true, true]":
				informationDecodee.add(true);
			}
		} 
		// on envoie le signal decode
		for ( DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationDecodee);
		}
		this.informationEmise = informationDecodee;
	}
}
