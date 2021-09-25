package transmetteurs;

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
	
	double esperance = (max + min)/2;
	double somme = 0;
	int i = 0;
	int j = 0;
	
	public DecodeurAnalogique(String forme, int nEch, float min, float max) 
	{
		
		super();
		
		this.forme = forme;
		this.nEch = nEch;
		this.min = min;
		this.max = max;
		informationNumerique = null;	}
	
	public void recevoir(Information<Float> information) throws InformationNonConformeException 
	{
		this.informationRecue = information;
		this.emettre();
	}

	public void emettre() throws InformationNonConformeException 
	{
		if (forme.equalsIgnoreCase("NRZT")) 
		{
			demodNRZT(nEch, max, min);
		} // continuer pour les autres encodages

		
		if(forme.equalsIgnoreCase("NRZ")) 
		{
			demodNRZT(nEch, max, min);
		}
		
		if(forme.equalsIgnoreCase("RZ")) 
		{
			demodRZ(nEch, max, min);
		}
		
		for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationNumerique);
		}
		this.informationEmise = informationNumerique;
		
	}
	
	public void demodNRZT(int nEch, float max, float min) throws InformationNonConformeException 
	{
		informationNumerique = new Information <Boolean> ();
		int nBits = informationRecue.nbElements()/nEch;
		
		
		for (double echantillon : informationRecue)
		{
			j++;
			somme += echantillon;
			
			if (j == nEch)
			{
				if (i==0)
				{
					if (somme / (2*nEch/3) > esperance)
						informationNumerique.add(true);
					else
						informationNumerique.add(false);
				}
				
				else if (i == nBits-1)
				{
					if (somme / (2*nEch/3) > esperance)
						informationNumerique.add(true);
					else
						informationNumerique.add(false);
				}
				
				else if (somme/nEch > esperance)
					informationNumerique.add(true);
				
				else
					informationNumerique.add(false);

				j = 0;
				i++;
				somme = 0;
			}
			
		}
		
	}
	
	/*public void demodNRZ(int nEch, float max, float min) throws InformationNonConformeException
	{
		informationNumerique = new Information <Boolean> ();
		
		for (int i = 0 ; i < informationRecue.nbElements() ; i++)
		{
            if(informationRecue.iemeElement(i) == max) 
            {
                informationNumerique.add(true);
            }
            else 
            {
            	informationNumerique.add(false);
            }
        }
	}*/
	
	public void demodRZ(int nEch, float max, float min) throws InformationNonConformeException
	{
		informationNumerique = new Information <Boolean> ();

		for (double echantillon : informationRecue)
		{
            j++;
            somme += echantillon;
			
			if (j == nEch)
			{
	            if(somme / nEch > (int)max/3)
					informationNumerique.add(true);
	            else
					informationNumerique.add(false);
	            
	            j = 0;
	            somme = 0;
	        }
		}
	}

}
