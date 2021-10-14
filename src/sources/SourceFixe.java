package sources;

import information.Information;

/**
 * Nom de classe 			: SourceFixe
 * 
 * Description 				: Cette classe produit un signal suivant la suite de 1 et de 0 entrée en arguments par l'utilisateur
 * 
 * Version 					: 1.0
 * 
 * Date 					: 14/10/2021
 * 
 * Copyright 				: Gurvan, Christopher, Alexandre, Aurelien Promotion 2023 FIP 2A
 * 
 */
public class SourceFixe extends Source<Boolean> {

	public SourceFixe (String message) {
		
		super();
		
		informationGeneree = new Information<Boolean>();
		
		// on v�rifie la forme du message
		for (int i=0 ; i < message.length() ; i++ ) {
			
			if ( message.charAt(i) == '0' ) {
				informationGeneree.add(false);
			}
			else {
				informationGeneree.add(true);
			}
			
		}
	}
		
}
