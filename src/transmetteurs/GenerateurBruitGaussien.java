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

}
