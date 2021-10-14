package sources;

import java.util.Random;

import information.Information;

/**
 * Nom de classe 			: SourceAleatoire
 * 
 * Description 				: Cette classe produit un signal aléatoire, suivant une seed ou non
 * 
 * Date 					: 14/10/2021
 * 
 * Copyright 				: Gurvan, Christopher, Alexandre, Aurelien Promotion 2023 FIP 2A
 * 
 */
public class SourceAleatoire extends Source<Boolean> {

	private Random r;
	
	public SourceAleatoire (String longueurMessage, Integer intSeed) {
		
		super();
		
		informationGeneree = new Information<Boolean>();
		
		if(intSeed == null) {
			r = new Random();
		} else if (intSeed != null) {
			Long seed = Long.valueOf(intSeed);
			
			r = new Random(seed);
		}
		// enlever les tests car déjà faits dans analyse arguments
		// il faut v�rifier si c'est une seed, si c'est la longueur du message, ou si c'est 100 par d�faut
		if ( longueurMessage.equalsIgnoreCase("100") ) {
			for ( int i=0 ; i<100 ; i++) {
				informationGeneree.add(r.nextBoolean());
			}
		} else if (longueurMessage.length() <= 6){
			for ( int i=0 ; i<Integer.parseInt(longueurMessage) ; i++) { // String -> int
				informationGeneree.add(r.nextBoolean());
			}
		} else if (longueurMessage.length() > 6) {
			System.out.println("La longueur de message entr�e n'est pas correct (doit �tre <= 6");
		}
		//System.out.println(informationGeneree.toString());
	}
}

