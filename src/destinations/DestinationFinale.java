package destinations;

import information.Information;
import information.InformationNonConformeException;

public class DestinationFinale extends Destination<Boolean> {

	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		
		this.informationRecue = information;
		
	}

}
