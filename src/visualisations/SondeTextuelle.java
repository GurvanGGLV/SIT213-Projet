package visualisations;
	
import information.Information;

/** 
 * Classe realisant l'affichage (textuel) d'information composee
 * d'elements de type T
 * @author prou
 */
public class SondeTextuelle <T> extends Sonde <T> {
   
    /**
     * pour construire une sonde textuelle
     * @param nom  le nom de la fenetre d'affichage
     */
    public SondeTextuelle(String nom) {
	super(nom);
    }
   
    public void recevoir (Information <T> information) { 		 		 	
	informationRecue = information;
	System.out.println(nom + " : " + information);
    }
}
