package visualisations;
	
import information.Information;
import destinations.Destination;

/** 
 * Classe Abstraite d'un composant destination réalisant un affichage
 * @author prou
 */
public  abstract class Sonde <T> extends Destination <T> {
   
    /**
     * nom de la fenêtre d'affichage
     */   
    protected String nom;
   
    /**
     *  
     * @param nom  le nom de la fenêtre d'affichage
     */   
    public Sonde(String nom) {
	this.nom = nom;
    }
		
    /**
     * pour recevoir et afficher l'information transmise par la source
     * qui nous est connectée
     * @param information  l'information  à recevoir
     */   
    public abstract void recevoir(Information <T> information);     
}
