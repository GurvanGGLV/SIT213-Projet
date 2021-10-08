package transmetteurs;

/**
 * Nom de classe 			: DecodeurAnalogique
 * 
 * Description 				: Cette classe se compose des methodes recevoir() et emettre() ainsi que 
 * 							  des methodes de transmission de l'information. Elle a pour but de reconstituer l'information numerique
 * 							  à partir de l'information analogique reçue (bruitee ou non).
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

	private String forme;
	private int nEch;
	private float min;
	private float max;
	protected Information<Boolean> informationNumerique;
	
	private double esperance;
	private double somme = 0;
	private int compteurEch = 0;
	//private int i = 0;
	
	public DecodeurAnalogique(String forme, int nEch, float min, float max) 
	{
		//super();
		
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
	{
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
	 * La methode decodeur permet de décoder l'information analogique reçue et codée par le code en ligne NRZT ou NRZ (No Return to Zero). 
	 * Pour cela, on fixe un seuil à la valeur (max + min)/2 puis on calcule la valeur moyenne de chaque bit reçu. Si celle-ci est
	 * au dessus du seuil, le bit reçu est 1, sinon c'est un 0.
	 *  
	 * @param nEch
	 * @throws InformationNonConformeException
	 * 
	 */
	
	public void decodeur(int nEch) throws InformationNonConformeException 
	{
		informationNumerique = new Information <Boolean> ();
		
		for (double echantillon : informationRecue)
		{
			compteurEch++;
			somme += echantillon;
			
			if (compteurEch == nEch)
			{	
				if (somme/nEch > esperance)
					informationNumerique.add(true);
				
				else
					informationNumerique.add(false);

				compteurEch = 0;
				somme = 0;
			}
			
		}
		
	}
	
	/**
	 * La methode decodRZ permet de décoder l'information analogique reçue et codée par le code en ligne RZ (Return to Zero). 
	 * Pour cela, on fixe un seuil à la valeur max/3 puis on calcule la valeur moyenne de chaque bit reçu. Si celle-ci est
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

		for (float echantillon : informationRecue)
		{
			compteurEch++;
            somme += echantillon;
			
			if (compteurEch == nEch)
			{
	            if(somme > max/2)
					informationNumerique.add(true);
	            else
					informationNumerique.add(false);
	            
	            compteurEch = 0;
	            somme = 0;
	        }
		}
	}
}
