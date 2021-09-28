package transmetteurs;

import java.util.Random;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class GenerateurBruitGaussien extends Transmetteur<Float,Float>{

	private int nbEch; // représente N = Te*Fe
	private float snr; // snr en dB
	protected Information<Float> informationBruitee;
	
	public GenerateurBruitGaussien(float snr, int nEch) {		
		
		//super();
		
		this.nbEch = nEch;
		this.snr = snr; 
		informationBruitee = null;
	}
	
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		this.informationRecue = information;
		this.emettre();
		
	}

	public void emettre() throws InformationNonConformeException {
		
		informationBruitee = new Information<Float>();
		
		float sigma = calculSigma();
		float bruit;
		
		// on ajoute le bruit au signal
		for(float s : informationRecue) {
			s+=calculBruit(sigma); // le float courant + le bruit calculé à cet instant
			informationBruitee.add(s); // on remplit informationBruitee
		}
		
		for(DestinationInterface<Float> destinationConnectee : destinationsConnectees ) {
			destinationConnectee.recevoir(informationBruitee);
		}
		this.informationEmise = informationBruitee;
	}
	
	/**
	 * Cette méthode permet de calculer la puissance du signal (1/K * somme(s(n)²))
	 * @param informationAnalogique
	 * 		- le signal dont la puissance sera calculée
	 * @return
	 * 		-puissanceMoyenneSignal : la puissance calculée
	 */
	public float calculPuissance(Information<Float> informationAnalogique) {
		
		float puissanceMoyenneSignal=0;
		float sommeSignaux=0;
		
		for(float f : informationAnalogique) {
			sommeSignaux += Math.pow(f,2); // somme(s(n)²)
		}
		
		puissanceMoyenneSignal = sommeSignaux/informationAnalogique.nbElements(); // 1/K
		
		return puissanceMoyenneSignal;
	}

	public float calculBruit(float sigmaB) {
		
		// création de 2 variables aléatoire uniformes
		Random a1 = new Random();
		Random a2 = new Random();
	
		// on retourne le calul présenté dans les documents de TP3
		return (float)(sigmaB*Math.sqrt(-2*Math.log(1-a1.nextFloat()))*Math.cos(2*Math.PI*a2.nextFloat()));
	}
	
	public float calculSigma() {
		
		float sigma;
		
		// on récupère la puissance moyenne du signal
		float ps = calculPuissance(informationRecue);
		
		// on passe en log le snr pour effectuer le calcul
		float logSnr = (float)Math.pow(10, snr/10);
		
		sigma = (float)Math.sqrt((ps*nbEch)/(2*logSnr));
		
		return sigma;
	}
	
}
