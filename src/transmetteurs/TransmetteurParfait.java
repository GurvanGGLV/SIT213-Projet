package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurParfait extends Transmetteur<Boolean, Boolean> {

	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		
		this.informationRecue = information;
		this.emettre();
	}

	public void emettre() throws InformationNonConformeException {
		
		for ( DestinationInterface<Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationRecue);
		}
		this.informationEmise = informationRecue;
	}

}
