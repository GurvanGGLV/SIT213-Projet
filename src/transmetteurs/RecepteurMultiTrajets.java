package transmetteurs;

import java.util.*;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/**
 * Cette classe recupere l'information multi trajets creee a partir de la classe EmetteutMultiTrajets.
 * Elle va la traiter de maniere a regenerer l'information initiale qui etait informationAnalogique
 * @author Gurvan, Aurelien
 *
 */
public class RecepteurMultiTrajets extends Transmetteur<Float,Float> {

	private ArrayList<Integer> listTaus;
	private ArrayList<Float> listAlphas;
	protected Information<Float> informationTiTraitee;
	
	public RecepteurMultiTrajets(ArrayList<Integer> taus, ArrayList<Float> alphas) {
		// faire une classe pour des taus et alphas invalides
		this.listTaus = taus;
		this.listAlphas = alphas;
		informationTiTraitee = null;
	}
	
	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		this.informationRecue = information; // information multi trajets
		this.emettre(); // on emet afin de nettoyer le signal des multi trajets
		
	}

	public void emettre() throws InformationNonConformeException {
		
		int longueurUtile = informationRecue.nbElements() - this.getTauMax(); // comme ça on connait la longueur reelle de notre signal
		informationTiTraitee = new Information<Float>(); // variable qui va contenir le signal nettoye des multi-trajets
		
		Information<Float> signalInitial = new Information<Float>();
		
		for(float inf : informationRecue) { // permet de stocker le signal de base 
			signalInitial.add(inf);
		}
		
		// ce qu'on veut maintenant c'est nettoyer le signal rajoute pour chaque decalage
		
		for(int pos=0 ; pos<longueurUtile ; pos++) { // on va regarder chaque echantillon jusqu'a la longueur utile du signal
			float temp = signalInitial.iemeElement(pos); // va nous servir a faire les operations de nettoyage
			// pour ensuite remplacer dans le nouveau signal
			for(int i=0 ; i<listTaus.size() ; i++) { // on fait l'operation inverse de l'emetteur
				
				int decalage = listTaus.get(i);
				if(pos>=decalage) { // on regarde quand on arrive au decalage
					// une fois qu'on est arrive au decalage 
					temp = temp - (listAlphas.get(i)*signalInitial.iemeElement(pos-decalage));
				}
			}
			// une fois qu'on a la valeur reelle de l'echantillon initial on remplace
			signalInitial.setIemeElement(pos, temp);
		}
		
		// quand on a enfin recupere le signal complet, sans les valeurs a 0 qui sont dues a tau max
		for(int i=0 ; i<longueurUtile ; i++) {
			informationTiTraitee.add(signalInitial.iemeElement(i));
		}
		
		// quand on a enfin recupere l'info initiale, on envoie
		for(DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationTiTraitee);
		}
		this.informationEmise = informationTiTraitee;
	}
	
	/**
	 * @return la valeur max du decalage
	 */
	public int getTauMax() {
		int tMax = listTaus.get(0);
		for(int t : listTaus) {
			if(t > tMax) {
				tMax = t;
			}
		}
		return tMax;
	}
	
	
}
