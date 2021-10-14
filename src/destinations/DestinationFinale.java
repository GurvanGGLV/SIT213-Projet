package destinations;

import information.Information;
import information.InformationNonConformeException;

/**
 * Nom de classe 			: DestinationFinale
 * 
 * Description 				: Cette classe est celle qui reçoit l'information finale après passage dans tous les blocs
 * 
 * Version 					: 2.0
 * 
 * Date 					: 14/10/2021
 * 
 * Copyright 				: Gurvan, Christopher, Alexandre, Aurelien Promotion 2023 FIP 2A
 * 
 */
public class DestinationFinale extends Destination<Boolean> {

	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {
		
		this.informationRecue = information;
		
	}

}
