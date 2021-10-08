package transmetteurs;

/**
 * Nom de classe 			: CodageCanal
 * 
 * Description 				: Cette classe se compose des methodes recevoir() et emettre() ainsi que 
 * 							  des methodes de codage du canal de l'information. Elle a pour but de formater l'information numerique
 * 							  en une nouvelle information analogique codée.
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
	
	public CodageCanal() {
	}
	
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		this.informationRecue = information;
		this.emettre();
	}

	/** 
	 * 
	 * La methode emettre() de cette classe formate l'information selon la table de correspondance associée.
	 * Elle instancie un attribut <informationCodee> qui sera envoyé au prochain élément de la chaine
	 * de transmission.
	 * 
	 */
	
	public void emettre() throws InformationNonConformeException {
		
		informationCodee = new Information<Boolean>();
		
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
	
		for ( DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationCodee);
		}
		this.informationEmise = informationCodee;	
	}
}
