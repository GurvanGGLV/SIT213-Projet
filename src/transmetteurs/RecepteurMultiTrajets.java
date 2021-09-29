package transmetteurs;

import java.util.ArrayList;

import information.Information;
import information.InformationNonConformeException;

/**
 * Cette classe récupère l'information multi trajets créée à partir de la classe EmetteutMultiTrajets.
 * Elle va la traiter de manière à régénérer l'information initiale qui était informationAnalogique
 * @author ggurv
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
		// TODO Auto-generated method stub
		
	}

	public void emettre() throws InformationNonConformeException {
		// TODO Auto-generated method stub
		
	}

	
	
}
