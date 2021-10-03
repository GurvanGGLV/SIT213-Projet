package transmetteurs;

import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/**
 * Cette classe va permettre de récupérer les amplitudes et décalages associés aux multi trajets
 * définis en arguments, et de procéder à l'emission en mode multi trajet du signal recue.
 * @author ggurv
 *
 */
public class EmetteurMultiTrajets extends Transmetteur<Float,Float>{

	
	private ArrayList<Integer> listTaus; // liste des tau entrés en arguments
	private ArrayList<Float> listAlphas; // liste des alphas entrés en arguments
	protected Information<Float>informationTi; // information en trajet indirect
	
	public EmetteurMultiTrajets(ArrayList<Integer> taus, ArrayList<Float> alphas) {
		// créer des classes exception
		this.listAlphas = alphas;
		this.listTaus = taus;
		informationTi = null;
	}

	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		
		// on récupère l'information 
		this.informationRecue = information;
		this.emettre(); // on emet ensuite afin d'obtenir le nouveau signal
	}

	// on emet le signal
	public void emettre() throws InformationNonConformeException {

		// on initialise la liste qui contiendra le multi trajet
		informationTi = new Information<Float>();

		
		// A GARDER AU CAS OU CA NE MARCHE PLUS EN FOR EACH
		/*
		// on rajoute l'information utile puis un nombre de 0 égale à la valeur du plus
		// grand décalage
		for (int i = 0; i < (informationRecue.nbElements() + this.getTauMax()); i++) {
			if (i < informationRecue.nbElements()) { // tant qu'on est dans l'information utile on l'écrit
				informationTi.add(informationRecue.iemeElement(i));
			} else { // quand on rentre dans le décalage on entre des 0
				informationTi.add(0f);
			}
		}*/
		
		// meme chose en for each
		for(float i : informationRecue) {
			informationTi.add(i);
		} for (int i=0 ; i<this.getTauMax() ; i++) {
			informationTi.add(0f);
		}

		// on veut maintenant respecter r(t) = s(t) +Aks(t-Tk) + b(t) , k appartenant à
		// [|0;5|]

		// alphaCourant va nous servir à récupérer le alpha correspondant au décalage
		// observé
		int alphaCourant = 0;

		for (int tau : listTaus) { // on va récupérer le signal initial, et le rajouter par décalage
			// dans le signal mutli trajet
			for (int pos = 0; pos <=informationTi.nbElements(); pos++) {
				if (pos >= tau && pos < informationRecue.nbElements()+tau) { // quand l'on atteint le premier décalage
					// on récupère la valeur de l'échantillon actuel
					float echCourant = informationTi.iemeElement(pos);
					// on récupère le premier echantillon équivalent dans le signal recu de base
					float echEq = informationRecue.iemeElement(pos-tau); // le -tau permet de récupérer
					// le premier échantillon du signal recu
					float alpha = listAlphas.get(alphaCourant);

					// on réaliser l'opération
					float echTi = echCourant + (alpha * echEq);

					// et on remplace à l'endroit où l'on est par l'échantillon initial +
					// l'échantillon du signal
					// recu * la nouvelle amplitude (0<=alpha<=1)
					informationTi.setIemeElement(pos, echTi);
				}
			}
			alphaCourant++;
		}
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationTi);
		}
		this.informationEmise = informationTi;
	}
	
	/**
	 * Méthode qui permet de récupérer le plus grand décalage en entrée du système
	 * @return
	 * 		- tMax : la valeur max du décalage
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
