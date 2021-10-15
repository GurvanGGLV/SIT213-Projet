package visualisations;
	
import information.Information;

/** 
 * Classe realisant l'affichage d'information composee d'elements
 * booleens
 * @author prou
 */
public class SondeLogique extends Sonde <Boolean> {
   
    /** le nombre de pixels en largeur pour un element d'information
     * Boolean a afficher dans la fenetre */
    private int nbPixels;
   
    /**
     * pour construire une sonde logique
     * @param nom  le nom de la fenetre d'affichage
     * @param nbPixels  le nombre pixels en largeur pour un element d'information Boolean a afficher dans la fenetre
     */
    public SondeLogique(String nom, int nbPixels) {
	super(nom);
	this.nbPixels = nbPixels;
    }
    
    public void recevoir (Information <Boolean> information) { 
	informationRecue = information;
	int nbElements = information.nbElements();
	boolean [] table = new boolean[nbElements];
	int i = 0;
	for (boolean b : information) {
            table[i] = b;
            i++;
	}
      	new VueCourbe (table,  nbPixels, nom); 
    }
}
