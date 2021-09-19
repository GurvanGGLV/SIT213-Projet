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
		if (forme.equalsIgnoreCase("NRZT")) 
		{
			demodNRZT(nEch, max, min);
		} // continuer pour les autres encodages

		
		if(forme.equalsIgnoreCase("NRZ")) 
		{
			demodNRZ(nEch, max, min);
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
		
		for (int i = (nEch/3); i<informationRecue.nbElements(); i=i+nEch)
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
	}
	
	public void demodNRZ(int nEch, float max, float min) throws InformationNonConformeException
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
	}
	
	public void demodRZ(int nEch, float max, float min) throws InformationNonConformeException
	{
		informationNumerique = new Information <Boolean> ();
		
		for (int i = (nEch/3)-1; i<informationRecue.nbElements(); i=i+nEch)
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
	}

}
