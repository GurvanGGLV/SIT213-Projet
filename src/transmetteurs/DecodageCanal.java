package transmetteurs;

import java.util.Iterator;

import destinations.DestinationInterface;

import java.util.Arrays;

import information.Information;
import information.InformationNonConformeException;

public class DecodageCanal extends Transmetteur<Boolean,Boolean> {
	
	
	protected Information<Boolean> informationDecodee;
	
	public DecodageCanal() {
		informationDecodee = null;
	}

	
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		this.informationRecue = information;
		this.emettre();
		
	}

	
	public void emettre() throws InformationNonConformeException {
		
		informationDecodee = new Information<Boolean>();
		
	
		
		for(Iterator<Boolean> iterator = informationRecue.iterator() ; iterator.hasNext();) 
		{
			
			Boolean bit1 = iterator.next(); // etude sur le premier bit 
			Boolean bit2 = iterator.next();	// etude sur le second bit
			Boolean bit3 = iterator.next(); // etude sur le troisieme bit 
			
			Boolean[] tabBit = {bit1 , bit2 , bit3};
			// sachant qu'on a les 3 bits voulus, on les convertit en string pour les comparer
			
			String tabBitString = Arrays.toString(tabBit);
			
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

			}
		}
		
		for ( DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationDecodee);
		}
		this.informationEmise = informationDecodee;
	}
}
