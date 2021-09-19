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
	
	public DecodeurAnalogique(String forme, int nEch, float min, float max) 
	{
		this.forme = forme;
		this.nEch = nEch;
		this.min = min;
		this.max = max;
		informationNumerique = null;	}
	
	@Override
	public void recevoir(Information<Float> information) throws InformationNonConformeException 
	{
		this.informationRecue = information;
		this.emettre();
	}

	@Override
	public void emettre() throws InformationNonConformeException 
	{
		if (forme.equalsIgnoreCase("NRZT")) {
			demodNRZT(nEch, max, min);
		} // continuer pour les autres encodages

		
		if(forme.equalsIgnoreCase("NRZ")) {
			
		}
		
		if(forme.equalsIgnoreCase("RZ")) {
			
		}
		
		for (DestinationInterface <Boolean> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationNumerique);
		}
		this.informationEmise = informationNumerique;
		
	}
	
	public void demodNRZT(int nEch, float max, float min) throws InformationNonConformeException 
	{
		informationNumerique = new Information <Boolean> ();
		
		Iterator <Float> bitCourant = informationRecue.iterator();
		
		for (int i = 0; i < informationRecue.nbElements(); i++) 
		{
			if(bitCourant.next() == 1)
			{
				
			}
		}
		
	}

}
