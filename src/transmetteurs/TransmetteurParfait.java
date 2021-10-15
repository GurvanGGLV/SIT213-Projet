package transmetteurs;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/**
 * Cette classe sert a transmettre sans alteration le signal a la destination dans le cas ou l'on est sans bruit, sans codeur etc...
 * @author ggurv
 *
 */
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
