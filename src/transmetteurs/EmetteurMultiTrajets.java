package transmetteurs;

import java.util.ArrayList;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

/**
 * Cette classe va permettre de recuperer les amplitudes et decalages associes aux multi trajets
 * definis en arguments, et de proceder a l'emission en mode multi trajet du signal recue.
 * @author ggurv
 *
 */
public class EmetteurMultiTrajets extends Transmetteur<Float,Float>{

	
	private ArrayList<Integer> listTaus; // liste des tau entres en arguments
	private ArrayList<Float> listAlphas; // liste des alphas entres en arguments
	protected Information<Float>informationTi; // information en trajet indirect
	
	public EmetteurMultiTrajets(ArrayList<Integer> taus, ArrayList<Float> alphas) {
		// creer des classes exception
		this.listAlphas = alphas;
		this.listTaus = taus;
		informationTi = null;
	}

	public void recevoir(Information<Float> information) throws InformationNonConformeException {
		
		// on recupere l'information 
		this.informationRecue = information;
		this.emettre(); // on emet ensuite afin d'obtenir le nouveau signal
	}

	// on emet le signal
	public void emettre() throws InformationNonConformeException {

		// on initialise la liste qui contiendra le multi trajet
		informationTi = new Information<Float>();

		// A GARDER AU CAS OU CA NE MARCHE PLUS EN FOR EACH
		/*
		// on rajoute l'information utile puis un nombre de 0 egale a la valeur du plus
		// grand decalage
		for (int i = 0; i < (informationRecue.nbElements() + this.getTauMax()); i++) {
			if (i < informationRecue.nbElements()) { // tant qu'on est dans l'information utile on l'ecrit
				informationTi.add(informationRecue.iemeElement(i));
			} else { // quand on rentre dans le decalage on entre des 0
				informationTi.add(0f);
			}
		}*/
		
		// meme chose en for each
		
		for(float i : informationRecue) {
			informationTi.add(i);
		} for (int i=0 ; i<this.getTauMax() ; i++) {
			informationTi.add(0f);
		}

		// on veut maintenant respecter r(t) = s(t) +Aks(t-Tk) + b(t) , k appartenant a
		// [|0;5|]

		// alphaCourant va nous servir a recuperer le alpha correspondant au decalage
		// observe
		int alphaCourant = 0;

		for (int tau : listTaus) { // on va recuperer le signal initial, et le rajouter par decalage
			// dans le signal mutli trajet
			for (int pos = 0; pos <=informationTi.nbElements(); pos++) {
				if (pos >= tau && pos < informationRecue.nbElements()+tau) { // quand l'on atteint le premier decalage
					// on recupere la valeur de l'echantillon actuel
					float echCourant = informationTi.iemeElement(pos);
					// on recupere le premier echantillon equivalent dans le signal recu de base
					float echEq = informationRecue.iemeElement(pos-tau); // le -tau permet de recuperer
					// le premier echantillon du signal recu
					float alpha = listAlphas.get(alphaCourant);

					// on realise l'operation
					float echTi = echCourant + (alpha * echEq);

					// et on remplace a l'endroit ou l'on est par l'echantillon initial +
					// l'echantillon du signal
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
	 * Methode qui permet de recuperer le plus grand decalage en entree du systeme
	 * @return
	 * 		- tMax : la valeur max du decalage
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
