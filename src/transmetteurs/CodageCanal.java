package transmetteurs;

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
