package transmetteurs;

import information.Information;
import information.InformationNonConformeException;

public class GenerateurBruitGaussien extends Transmetteur<Float,Float>{

	private int nbEch;
	private float signalSBruit;
	protected Information<Float> informationBruitee;
	
	public GenerateurBruitGaussien(int nEch, float snr) {		
		this.nbEch = nEch;
		this.signalSBruit = snr;
		informationBruitee = null;
	}
	
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		this.informationRecue = information;
		this.emettre();
		
	}

	public void emettre() throws InformationNonConformeException {
		
		
	}
	
	/**
	 * Cette méthode permet de calculer la puissance du signal
	 * @param informationAnalogique
	 * 		- le signal dont la puissance sera calculée
	 * @return
	 * 		-pS : la puissance calculée
	 */
	public float calculPuissance(Information<Float> informationAnalogique) {
		
		float pS=0;
		
		for(float f : informationAnalogique) {
			pS += Math.pow(f,2);
		}
		return pS;
	}

}
