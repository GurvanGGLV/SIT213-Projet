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
		
		float pasP = calculPasPositif(nEch, max);
		float pasN = calculPasNegatif(nEch, min);
		float dist = nEch / 3;
		float init = 0;
		
		Iterator <Float> bitCourant = informationRecue.iterator();
		
		for (int i = 0; i < informationRecue.nbElements(); i++) 
		{
			init = init + dist;
			if(bitCourant.next() == max)
			{
				for(int j = 0; j < nEch; j++)
				{
					informationNumerique.add(true);
				}
				
			}
			else
			{
				for(int j = 0; j < nEch; j++)
				{
					informationNumerique.add(false);
				}
			}
		}
	}
	
	
	/**
	 * Cette méthode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param nEch - nombre d'échantillons du signal
	 * @return - un float étant la valeur du pas
	 */
	public float calculPasPositif(int nEch, float max) {

		float xA = 0;
		float xB = (float) nEch / 3;
		float yA = 0;
		float yB = max;

		float pas = (yB - yA) / (xB - xA);

		return pas;
	}

	/**
	 * Cette méthode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param nEch - nombre d'échantillons du signal
	 * @return - un float étant la valeur du pas
	 */
	public float calculPasNegatif(int nEch, float min) {

		float xA = 0;
		float xB = (float) nEch / 3;
		float yA = 0;
		float yB = min;

		float pas = (yB - yA) / (xB - xA);

		return pas;
	}

}
