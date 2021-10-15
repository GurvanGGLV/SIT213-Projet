package visualisations;
	
import information.Information;
import destinations.Destination;

/** 
 * Classe Abstraite d'un composant destination realisant un affichage
 * @author prou
 */
public  abstract class Sonde <T> extends Destination <T> {
   
    /**
     * nom de la fenetre d'affichage
     */   
    protected String nom;
   
    /**
     *  
     * @param nom  le nom de la fenetre d'affichage
     */   
    public Sonde(String nom) {
	this.nom = nom;
    }
		
    /**
     * pour recevoir et afficher l'information transmise par la source
     * qui nous est connectee
     * @param information  l'information  a recevoir
     */   
    public abstract void recevoir(Information <T> information);     
}
