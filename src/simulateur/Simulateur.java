package simulateur;
import destinations.Destination;
import destinations.DestinationFinale;
import information.Information;
import sources.Source;
import sources.SourceAleatoire;
import sources.SourceFixe;
import transmetteurs.Transmetteur;
import transmetteurs.TransmetteurParfait;
import visualisations.Sonde;
import visualisations.SondeLogique;


/** La classe Simulateur permet de construire et simuler une chaîne de
 * transmission composée d'une Source, d'un nombre variable de
 * Transmetteur(s) et d'une Destination.
 * @author cousin
 * @author prou
 *
 */
public class Simulateur {
      	
    /** indique si le Simulateur utilise des sondes d'affichage */
    private boolean affichage = false;
    
    /** indique si le Simulateur utilise un message généré de manière aléatoire (message imposé sinon) */
    private boolean messageAleatoire = true;
    
    /** indique si le Simulateur utilise un germe pour initialiser les générateurs aléatoires */
    private boolean aleatoireAvecGerme = false;
    
    /** la valeur de la semence utilisée pour les générateurs aléatoires */
    private Integer seed = null; // pas de semence par défaut
    
    /** la longueur du message aléatoire à  transmettre si un message n'est pas imposé */
    private int nbBitsMess = 100; 
    
    /** la chaîne de caractères correspondant à  m dans l'argument -mess m */
    private String messageString = "100";
   
   	
    /** le  composant Source de la chaine de transmission */
    private Source <Boolean>  source = null;
    
    /** le  composant Transmetteur parfait logique de la chaine de transmission */
    private Transmetteur <Boolean, Boolean>  transmetteurLogique = null;
    
    /** le  composant Destination de la chaine de transmission */
    private Destination <Boolean>  destination = null;
   	
   
    /** Le constructeur de Simulateur construit une chaîne de
     * transmission composée d'une Source <Boolean>, d'une Destination
     * <Boolean> et de Transmetteur(s) [voir la méthode
     * analyseArguments]...  <br> Les différents composants de la
     * chaîne de transmission (Source, Transmetteur(s), Destination,
     * Sonde(s) de visualisation) sont créés et connectés.
     * @param args le tableau des différents arguments.
     *
     * @throws ArgumentsException si un des arguments est incorrect
     *
     */   
    public  Simulateur(String [] args) throws ArgumentsException {
    	
    	// analyser et récupérer les arguments   	
    	analyseArguments(args);
 
    	// instanciation de la source suivant la valeur des arguments
    	if ( messageAleatoire == true ) {
    		source = new SourceAleatoire(messageString, seed);
    	} else if ( messageAleatoire == false ) {
    		source = new SourceFixe(messageString);
    	}
    	
    	// instanciation de(s) transmetteur(s)
    	transmetteurLogique = new TransmetteurParfait();
    	
    	// instanciation de la destination
      	destination = new DestinationFinale();
      	
    	// sonde
    	if ( affichage == true ) {
    		Sonde<Boolean> sondeE = new SondeLogique("Sonde Entree", 10);
    		Sonde<Boolean> sondeS = new SondeLogique("Sonde Sortie", 10);
    		source.connecter(sondeE);
    		transmetteurLogique.connecter(sondeS);
    	}
      	
      	// connexion des différents composants entre eux
      	source.connecter(transmetteurLogique);
      	transmetteurLogique.connecter(destination);

    }
   
   
   
    /** La méthode analyseArguments extrait d'un tableau de chaînes de
     * caractères les différentes options de la simulation.  <br>Elle met
     * à  jour les attributs correspondants du Simulateur.
     *
     * @param args le tableau des différents arguments.
     * <br>
     * <br>Les arguments autorisés sont : 
     * <br> 
     * <dl>
     * <dt> -mess m  </dt><dd> m (String) constitué de 7 ou plus digits à  0 | 1, le message à  transmettre</dd>
     * <dt> -mess m  </dt><dd> m (int) constitué de 1 à  6 digits, le nombre de bits du message "aléatoire" à Â transmettre</dd> 
     * <dt> -s </dt><dd> pour demander l'utilisation des sondes d'affichage</dd>
     * <dt> -seed v </dt><dd> v (int) d'initialisation pour les générateurs aléatoires</dd> 
     * </dl>
     *
     * @throws ArgumentsException si un des arguments est incorrect.
     *
     */   
    public  void analyseArguments(String[] args)  throws  ArgumentsException {

    	for (int i=0;i<args.length;i++){ // traiter les arguments 1 par 1

    		if (args[i].matches("-s")){
    			affichage = true;
    		}
    		
    		else if (args[i].matches("-seed")) {
    			aleatoireAvecGerme = true;
    			i++; 
    			// traiter la valeur associee
    			try { 
    				seed = Integer.valueOf(args[i]);
    			}
    			catch (Exception e) {
    				throw new ArgumentsException("Valeur du parametre -seed  invalide :" + args[i]);
    			}           		
    		}

    		else if (args[i].matches("-mess")){
    			i++; 
    			// traiter la valeur associee
    			messageString = args[i];
    			if (args[i].matches("[0,1]{7,}")) { // au moins 7 digits
    				messageAleatoire = false;
    				nbBitsMess = args[i].length();
    			} 
    			else if (args[i].matches("[0-9]{1,6}")) { // de 1 à  6 chiffres
    				messageAleatoire = true;
    				nbBitsMess = Integer.valueOf(args[i]);
    				if (nbBitsMess < 1) 
    					throw new ArgumentsException ("Valeur du parametre -mess invalide : " + nbBitsMess);
    			}
    			else 
    				throw new ArgumentsException("Valeur du parametre -mess invalide : " + args[i]);
    		}
    		
    		//TODO : ajouter ci-après le traitement des nouvelles options

    		else throw new ArgumentsException("Option invalide :"+ args[i]);
    	}
      
    }
     
    
   	
    /** La méthode execute effectue un envoi de message par la source
     * de la chaîne de transmission du Simulateur.
     *
     * @throws Exception si un problème survient lors de l'exécution
     *
     */ 
    public void execute() throws Exception {      
    	source.emettre();
    }
   
   	   	
   	
    /** La méthode qui calcule le taux d'erreur binaire en comparant
     * les bits du message émis avec ceux du message reà§u.
     *
     * @return  La valeur du Taux dErreur Binaire.
     */   	   
    public float  calculTauxErreurBinaire() {

    	// réception
    	Information<Boolean> signalRecu = destination.getInformationRecue();
    	
    	// calcul
    	Information<Boolean> signalEmis = source.getInformationEmise();
    	int longueurEmission = signalEmis.nbElements();
    	int nbErreurs = 0;
    	
    	for ( int i=0 ; i<longueurEmission ; i++) {
    		if ( signalRecu.iemeElement(i) != signalEmis.iemeElement(i) ) {
    			nbErreurs += 1;
    		}
    	}
    	float teb = ((float)nbErreurs/(float)longueurEmission)*100;
    	return  teb;
    }
   
   
   
   
    /** La fonction main instancie un Simulateur à  l'aide des
     *  arguments paramètres et affiche le résultat de l'exécution
     *  d'une transmission.
     *  @param args les différents arguments qui serviront à  l'instanciation du Simulateur.
     */
    public static void main(String [] args) { 

    	Simulateur simulateur = null;

    	try {
    		simulateur = new Simulateur(args);
    	}
    	catch (Exception e) {
    		System.out.println(e); 
    		System.exit(-1);
    	} 

    	try {
    		simulateur.execute();
    		String s = "java  Simulateur  ";
    		for (int i = 0; i < args.length; i++) { //copier tous les paramètres de simulation
    			s += args[i] + "  ";
    		}
    		System.out.println(s + "  =>   TEB : " + simulateur.calculTauxErreurBinaire());
    	}
    	catch (Exception e) {
    		System.out.println(e);
    		e.printStackTrace();
    		System.exit(-2);
    	}              	
    }
}

