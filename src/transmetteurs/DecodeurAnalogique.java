package transmetteurs;

/**
 * Nom de classe 			: DecodeurAnalogique
 * 
 * Description 				: Cette classe se compose des methodes recevoir() et emettre() ainsi que 
 * 							  des methodes de transmission de l'information. Elle a pour but de reconstituer l'information numerique
 * 							  a partir de l'information analogique reçue (bruitee ou non).
 * 
 * Version 					: 2.0
 * 
 * Date 					: 24/09/2021
 * 
 * Copyright 				: Gurvan, Christopher, Alexandre, Aurelien Promotion 2023 FIP 2A
 * 
 */

import java.util.Iterator;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;
import transmetteurs.EmetteurAnalogique;

public class DecodeurAnalogique extends Transmetteur<Float, Boolean> 
{

	private String forme; // va nous permettre de choisir le type de decodage
	private int nEch; // sert aux calculs
	private float min; // sert aux calculs
	private float max; // sert aux calculs
	protected Information<Boolean> informationNumerique; // va contenir l'information decodee
	
	private double esperance; // variable nous servant a faire de la prise de decision
	private double somme = 0; // cette variable va contenir la somme de tous les echantillons sur un tBit
	private int compteurEch = 0;
	
	/**
	 * Constructeur de la classe qui initialise les variables grace aux parametres ci dessous
	 * @param forme
	 * @param nEch
	 * @param min
	 * @param max
	 */
	public DecodeurAnalogique(String forme, int nEch, float min, float max) 
	{	
		this.forme = forme;
		this.nEch = nEch;
		this.min = min;
		this.max = max;
		this.esperance = (max + min)/2;
		informationNumerique = null;	}
	
	public void recevoir(Information<Float> information) throws InformationNonConformeException 
	{
		this.informationRecue = information;
		this.emettre();
	}

	public void emettre() throws InformationNonConformeException 
	{	// il faut choisir quel decodeur va etre utilise
		if (forme.equalsIgnoreCase("NRZT") || forme.equalsIgnoreCase("NRZ")) 
		{
			decodeur(nEch);
		}
	
		if(forme.equalsIgnoreCase("RZ")) 
		{
			decodRZ(nEch, max);
		}
		
		for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationNumerique);
		}
		this.informationEmise = informationNumerique;
		
	}
	
	
	/**
	 * La methode decodeur permet de decoder l'information analogique reçue et codee par le code en ligne NRZT ou NRZ (No Return to Zero). 
	 * Pour cela, on fixe un seuil a la valeur (max + min)/2 puis on calcule la valeur moyenne de chaque bit reçu. Si celle-ci est
	 * au dessus du seuil, le bit reçu est 1, sinon c'est un 0.
	 *  
	 * @param nEch
	 * @throws InformationNonConformeException
	 * 
	 */
	public void decodeur(int nEch) throws InformationNonConformeException 
	{
		informationNumerique = new Information <Boolean> ();
		
		// on parcours l'information
		for (double echantillon : informationRecue)
		{
			compteurEch++; 
			somme += echantillon;
			
			if (compteurEch == nEch) // lorsque que l'on est passe sur un bit  on peut prendre la decision qui convient
			{	
				if (somme/nEch > esperance) // si la moyenne est superieure a l'esperance on met un true, false sinon
					informationNumerique.add(true);
				
				else
					informationNumerique.add(false);

				compteurEch = 0;
				somme = 0;
			}
			
		}
		
	}
	
	/**
	 * La methode decodRZ permet de decoder l'information analogique reçue et codee par le code en ligne RZ (Return to Zero). 
	 * Pour cela, on fixe un seuil a la valeur max/3 puis on calcule la valeur moyenne de chaque bit reçu. Si celle-ci est
	 * au dessus du seuil, le bit reçu est 1, sinon c'est un 0.
	 *  
	 * @param nEch
	 * @param max
	 * @throws InformationNonConformeException
	 * 
	 */
	public void decodRZ(int nEch, float max) throws InformationNonConformeException
	{
		informationNumerique = new Information <Boolean> ();

		// meme systeme que pour l'autre methode
		for (float echantillon : informationRecue)
		{
			compteurEch++;
            somme += echantillon;
			
			if (compteurEch == nEch)
			{
	            if((somme/(nEch/3) > (max/2))) { // la prise de decision est differente car 2/3 tBit sont a 0
					informationNumerique.add(true);
	            } else {
					informationNumerique.add(false);
	            }
	            compteurEch = 0;
	            somme = 0;
	        }
		}
	}
}
