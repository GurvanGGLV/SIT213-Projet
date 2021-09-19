package simulateur;
import destinations.Destination;
import destinations.DestinationFinale;
import information.Information;
import sources.Source;
import sources.SourceAleatoire;
import sources.SourceFixe;
import transmetteurs.DecodeurAnalogique;
import transmetteurs.EmetteurAnalogique;
import transmetteurs.Transmetteur;
import transmetteurs.TransmetteurParfait;
import visualisations.Sonde;
import visualisations.SondeAnalogique;
import visualisations.SondeLogique;


/** La classe Simulateur permet de construire et simuler une cha�ne de
 * transmission compos�e d'une Source, d'un nombre variable de
 * Transmetteur(s) et d'une Destination.
 * @author cousin
 * @author prou
 *
 */
public class Simulateur {
      	
    /** indique si le Simulateur utilise des sondes d'affichage */
    private boolean affichage = false;
    
    /** indique si le Simulateur utilise un message g�n�r� de mani�re al�atoire (message impos� sinon) */
    private boolean messageAleatoire = true;
    
    /** indique si le Simulateur utilise un germe pour initialiser les g�n�rateurs al�atoires */
    private boolean aleatoireAvecGerme = false;
    
    /** la valeur de la semence utilis�e pour les g�n�rateurs al�atoires */
    private Integer seed = null; // pas de semence par d�faut
    
    /** la longueur du message al�atoire � transmettre si un message n'est pas impos� */
    private int nbBitsMess = 100; 
    
    /** la cha�ne de caract�res correspondant � m dans l'argument -mess m */
    private String messageString = "100";
   
   	
    /** le  composant Source de la chaine de transmission */
    private Source <Boolean>  source = null;
    
    /** le  composant Transmetteur parfait logique de la chaine de transmission */
    private Transmetteur <Boolean, Boolean>  transmetteurLogique = null;
    
    /** le  composant Destination de la chaine de transmission */
    private Destination <Boolean>  destination = null;
   	
    /** indique si la transmition est analogique */
    private boolean transAnalogique = false;
    
    /** le nombre d'échantillons */
	private int ne = 30;
	
	/** l'amplitude max et min */
	private float max = 1.0f;
	private float min = 0.0f;
	
	/** indique l'utilisation d'un codeur */
	//private boolean codeur = false;
	
	/** indique le type de forme pour le signal analogique */
	private String form = "NRZ";
	
    /** Le constructeur de Simulateur construit une cha�ne de
     * transmission compos�e d'une Source <Boolean>, d'une Destination
     * <Boolean> et de Transmetteur(s) [voir la m�thode
     * analyseArguments]...  <br> Les diff�rents composants de la
     * cha�ne de transmission (Source, Transmetteur(s), Destination,
     * Sonde(s) de visualisation) sont cr��s et connect�s.
     * @param args le tableau des diff�rents arguments.
     *
     * @throws ArgumentsException si un des arguments est incorrect
     *
     */   
    public  Simulateur(String [] args) throws ArgumentsException {
    	
    	// analyser et r�cup�rer les arguments   	
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
      	    
      	// création d'un emetteur analogique si précisé en argument
      	if ( transAnalogique == true ) {
      		EmetteurAnalogique emetteurAnalogique = new EmetteurAnalogique(form, ne, min, max); 
      		DecodeurAnalogique decodeurAnalogique = new DecodeurAnalogique(form, ne, min, max);
      		source.connecter(emetteurAnalogique);
      		emetteurAnalogique.connecter(decodeurAnalogique);
      		if (affichage == true) {
    			Sonde<Float> sondeE = new SondeAnalogique("Sonde Analogique Entree");
    			Sonde<Boolean> sondeL = new SondeLogique("Sonde Logique", 10);
    			emetteurAnalogique.connecter(sondeE);
    			source.connecter(sondeL);
    			//decodeurAnalogique.connecter(destinationFinale);
    			// + faire une nouvelle sonde logique qui récupère le signal logique
      		}
      	}
      	
    	// sonde
    	if ( affichage == true ) {
    		if (transAnalogique == false) {
    			Sonde<Boolean> sondeE = new SondeLogique("Sonde Entree", 10);
    			Sonde<Boolean> sondeS = new SondeLogique("Sonde Sortie", 10);
        		source.connecter(sondeE);
        		transmetteurLogique.connecter(sondeS);
    	}

      	// connexion des diff�rents composants entre eux
      	source.connecter(transmetteurLogique);
      	transmetteurLogique.connecter(destination);

    	}
    }
   
   
   
    /** La m�thode analyseArguments extrait d'un tableau de cha�nes de
     * caract�res les diff�rentes options de la simulation.  <br>Elle met
     * � jour les attributs correspondants du Simulateur.
     *
     * @param args le tableau des diff�rents arguments.
     * <br>
     * <br>Les arguments autoris�s sont : 
     * <br> 
     * <dl>
     * <dt> -mess m  </dt><dd> m (String) constitu� de 7 ou plus digits � 0 | 1, le message � transmettre</dd>
     * <dt> -mess m  </dt><dd> m (int) constitu� de 1 � 6 digits, le nombre de bits du message "al�atoire" � transmettre</dd> 
     * <dt> -s </dt><dd> pour demander l'utilisation des sondes d'affichage</dd>
     * <dt> -seed v </dt><dd> v (int) d'initialisation pour les g�n�rateurs al�atoires</dd> 
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
    			else if (args[i].matches("[0-9]{1,6}")) { // de 1 � 6 chiffres
    				messageAleatoire = true;
    				nbBitsMess = Integer.valueOf(args[i]);
    				if (nbBitsMess < 1) 
    					throw new ArgumentsException ("Valeur du parametre -mess invalide : " + nbBitsMess);
    			}
    			else 
    				throw new ArgumentsException("Valeur du parametre -mess invalide : " + args[i]);
    		}    
    		
    		else if (args[i].matches("-nbEch")) {
    			transAnalogique = true;
    			i++;
    			try {
    				ne = Integer.valueOf(args[i]);
    			} catch (Exception e) {
    				throw new ArgumentsException("Valeur du parametre -nbEch invalide : " + args[i]);
    			}
    		}
    		
    		else if (args[i].matches("-ampl")) {
    			transAnalogique = true;
    			i++;
    			try {
    				min = Float.valueOf(args[i]);
    				i++;
    				max = Float.valueOf(args[i]);
    			} catch (Exception e) {
    				throw new ArgumentsException("Valeurs des amplitudes invalides : " + args[i--] + " | " + args[i]);
    			}
    		}
    		else if (args[i].matches("-form")) {
    			transAnalogique = true;
    			i++;
    			try {
    				form = args[i];
    			} catch (Exception e) {
    				throw new ArgumentsException("Valeur de la forme est invalide : " + args[i]);
    			}
    		}
    		
    		//TODO : ajouter ci-apr�s le traitement des nouvelles options

    		else throw new ArgumentsException("Option invalide :"+ args[i]);
    	}
      
    }
     
    
   	
    /** La m�thode execute effectue un envoi de message par la source
     * de la cha�ne de transmission du Simulateur.
     *
     * @throws Exception si un probl�me survient lors de l'ex�cution
     *
     */ 
    public void execute() throws Exception {      
    	source.emettre();
    }
   
   	   	
   	
    /** La m�thode qui calcule le taux d'erreur binaire en comparant
     * les bits du message �mis avec ceux du message re�u.
     *
     * @return  La valeur du Taux dErreur Binaire.
     */   	   
    public float  calculTauxErreurBinaire() {

    	// r�ception
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
   
   
   
   
    /** La fonction main instancie un Simulateur � l'aide des
     *  arguments param�tres et affiche le r�sultat de l'ex�cution
     *  d'une transmission.
     *  @param args les diff�rents arguments qui serviront � l'instanciation du Simulateur.
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
    		for (int i = 0; i < args.length; i++) { //copier tous les param�tres de simulation
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

