package visualisations;
	
import information.Information;

/** 
 * Classe réalisant l'affichage de la puissance d'une information
 * composée d'éléments de type réel (float)
 * @author prou
 */
public class SondePuissance extends Sonde <Float> {
   
    /**
     * pour construire une sonde puissance
     * @param nom  le nom de la fenêtre d'affichage
     */
    public SondePuissance(String nom) {
	super(nom);
    }
   	 
    public void recevoir (Information <Float> information) { 
	informationRecue = information;
	int nbElements = information.nbElements();
	Double puissance = 0.0;
	for (int i = 0; i < nbElements; i++) {
            puissance +=  information.iemeElement(i) *  information.iemeElement(i);
	}
	puissance = puissance / nbElements;
	new VueValeur (puissance,  nom); 
    }
}
