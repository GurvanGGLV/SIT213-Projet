package simulateur;

import java.util.ArrayList;

import destinations.Destination;
import destinations.DestinationFinale;
import information.Information;
import sources.Source;
import sources.SourceAleatoire;
import sources.SourceFixe;
import transmetteurs.CodageCanal;
import transmetteurs.DecodageCanal;
import transmetteurs.DecodeurAnalogique;
import transmetteurs.EmetteurAnalogique;
import transmetteurs.EmetteurMultiTrajets;
import transmetteurs.GenerateurBruitGaussien;
import transmetteurs.RecepteurMultiTrajets;
import transmetteurs.Transmetteur;
import transmetteurs.TransmetteurParfait;
import visualisations.Sonde;
import visualisations.SondeAnalogique;
import visualisations.SondeLogique;
import java.util.Iterator;

/**
 * 
 * La classe Simulateur permet de construire et simuler une chaene de
 * transmission composee d'une Source, d'un nombre variable de Transmetteur(s)
 * et d'une Destination.
 * 
 * @author cousin
 * @author prou
 * 
 *         Etudiant : Christopher, Gurvan, Aurelien, Alexandre promotion FIP2023
 *         FIP2A
 *
 */
public class Simulateur {

	/** indique si le Simulateur utilise des sondes d'affichage */
	private boolean affichage = false;

	/**
	 * indique si le Simulateur utilise un message genere de maniere aleatoire
	 * (message impose sinon)
	 */
	private boolean messageAleatoire = true;

	/**
	 * indique si le Simulateur utilise un germe pour initialiser les generateurs
	 * aleatoires
	 */
	private boolean aleatoireAvecGerme = false;

	/** la valeur de la semence utilisee pour les generateurs aleatoires */
	private Integer seed = null; // pas de semence par defaut

	/**
	 * la longueur du message aleatoire e transmettre si un message n'est pas impose
	 */
	private int nbBitsMess = 100;

	/** la chaene de caracteres correspondant e m dans l'argument -mess m */
	private String messageString = "100";

	/** le composant Source de la chaine de transmission */
	private Source<Boolean> source = null;

	/** le composant Transmetteur parfait logique de la chaine de transmission */
	private Transmetteur<Boolean, Boolean> transmetteurLogique = null;

	/** le composant Destination de la chaine de transmission */
	private Destination<Boolean> destination = null;
	
	/** indique si la transmition est analogique */
	private boolean transAnalogique = false;

	/** indique si la transmition est bruitee pour une transmission analogique */
	private boolean transBruitee = false;

	/** indique si la transmition est multitrajet pour une transmission analogique*/
	private boolean transMultiTraj = false;
	
	/** la valeur du rapport signal sur bruit entree en argument */
	private float snr = 0;

	/** le nombre d'echantillons */
	private int ne = 30;

	/** l'amplitude max et min */
	private float max = 1.0f;
	
	private float min = 0.0f;

	/** indique l'utilisation d'un codeur */
	private Boolean codage = false;

	/** indique le type de forme pour le signal analogique */
	private String form = "RZ";
	
	
	/** indique la presence de trajets multiples */
	ArrayList<Integer> listTaus = new ArrayList<>();
	ArrayList<Float> listAlphas = new ArrayList<>();

	/**
	 * Le constructeur de Simulateur construit une chaene de transmission composee
	 * d'une Source <Boolean>, d'une Destination <Boolean> et de Transmetteur(s)
	 * [voir la methode analyseArguments]... <br>
	 * Les differents composants de la chaene de transmission (Source,
	 * Transmetteur(s), Destination, Sonde(s) de visualisation) sont crees et
	 * connectes.
	 * 
	 * @param args le tableau des differents arguments.
	 *
	 * @throws ArgumentsException si un des arguments est incorrect
	 *
	 */
	public Simulateur(String[] args) throws ArgumentsException {

		// analyser et recuperer les arguments
		analyseArguments(args);

		// instanciation de la source suivant la valeur des arguments
		if (messageAleatoire == true) {
			source = new SourceAleatoire(messageString, seed);
		} else if (messageAleatoire == false) {
			source = new SourceFixe(messageString);
		}
		
		// instanciation de(s) transmetteur(s)
		transmetteurLogique = new TransmetteurParfait();

		// instanciation de la destination
		destination = new DestinationFinale();

		// creation d'un emetteur analogique si precise en argument
		
		if(codage == true ) { // si la transmission est codee
			// instanciation du codage Canal
			CodageCanal codageCanal = new CodageCanal();
			DecodageCanal decodageCanal = new DecodageCanal();
			
			if (transAnalogique == true) { //si la transmission est analogique
	
				// instanciation de l'emetteur et du decodeur
				EmetteurAnalogique emetteurAnalogique = new EmetteurAnalogique(form, ne, min, max);
				DecodeurAnalogique decodeurAnalogique = new DecodeurAnalogique(form, ne, min, max);
	
				if (transBruitee == true) { // si la transmission est bruitee
					// instanciation du generateur de bruit
					GenerateurBruitGaussien generateurBruit = new GenerateurBruitGaussien(snr, ne);
					/*
					if (aleatoireAvecGerme) {
						generateurBruit = new GenerateurBruitGaussien(snr, ne, seed);
					} else {
						generateurBruit = new GenerateurBruitGaussien(snr, ne);
					}*/
					
					if(transMultiTraj == true) { // si la transmission subit des decalages
						//instanciation du multi-trajet
						EmetteurMultiTrajets emetteurMT = new EmetteurMultiTrajets(listTaus, listAlphas);
						//RecepteurMultiTrajets recepteurMT = new RecepteurMultiTrajets(listTaus, listAlphas);
						
						//connexion des differents elements du systeme
						source.connecter(codageCanal);
						codageCanal.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(emetteurMT);
						emetteurMT.connecter(generateurBruit);
						generateurBruit.connecter(decodeurAnalogique);
						//recepteurMT.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(decodageCanal);
						decodageCanal.connecter(destination);
						
						if (affichage == true) { //si l'on souhaite afficher les graphes
	
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Float> sondeB = new SondeAnalogique("Signal Bruite");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Sortie Decodeur", 10);
							Sonde<Float> sondeEMT = new SondeAnalogique("Signal sortie emetteur MT");
							Sonde<Float> sondeRMT = new SondeAnalogique("Signal sortie recepteur MT");
							Sonde<Boolean> sondeCodage = new SondeLogique("Sonde Sortie Codage", 10);
							Sonde<Boolean> sondeDeodage = new SondeLogique("Sonde Sortie Decodage", 10);
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							generateurBruit.connecter(sondeB);
							decodeurAnalogique.connecter(sondeC);
							emetteurMT.connecter(sondeEMT);
							//recepteurMT.connecter(sondeRMT);
							codageCanal.connecter(sondeCodage);
							decodageCanal.connecter(sondeDeodage);
						}
						
					} else { // si la transmission ne subit pas de decalage
						//connexion des differents elements du systeme
						source.connecter(codageCanal);
						codageCanal.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(generateurBruit);
						generateurBruit.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(decodageCanal);
						decodageCanal.connecter(destination);
						
						if (affichage == true) { //si l'on souhaite afficher les graphes
							
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Float> sondeB = new SondeAnalogique("Signal Bruite");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Recepteur", 10);
							Sonde<Boolean> sondeCodage = new SondeLogique("Sonde Sortie Codage", 10);
							Sonde<Boolean> sondeDeodage = new SondeLogique("Sonde Sortie Decodage", 10);
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							generateurBruit.connecter(sondeB);
							decodeurAnalogique.connecter(sondeC);
							codageCanal.connecter(sondeCodage);
							decodageCanal.connecter(sondeDeodage);
						}
					}
	
				} else { // si la transmission n'est pas bruitee
	
					if(transMultiTraj == true) { // si la transmission subit des decalages
						EmetteurMultiTrajets emetteurMT = new EmetteurMultiTrajets(listTaus, listAlphas);
						RecepteurMultiTrajets recepteurMT = new RecepteurMultiTrajets(listTaus, listAlphas);
						
						//connexion des differents elements du systeme
						source.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(emetteurMT);
						emetteurMT.connecter(recepteurMT);
						recepteurMT.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(destination);
						
						if (affichage == true) {
	
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Sortie Decodeur", 10);
							Sonde<Float> sondeEMT = new SondeAnalogique("Signal sortie emetteur MT");
							Sonde<Float> sondeRMT = new SondeAnalogique("Signal sortie recepteur MT");
							Sonde<Boolean> sondeCodage = new SondeLogique("Sonde Sortie Codage", 10);
							Sonde<Boolean> sondeDeodage = new SondeLogique("Sonde Sortie Decodage", 10);
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							decodeurAnalogique.connecter(sondeC);
							emetteurMT.connecter(sondeEMT);
							recepteurMT.connecter(sondeRMT);
							codageCanal.connecter(sondeCodage);
							decodageCanal.connecter(sondeDeodage);
						}
					} else { // si la transmission ne subit pas de decalage
						//connexion des differents elements du systeme
						source.connecter(codageCanal);
						codageCanal.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(decodageCanal);
						decodageCanal.connecter(destination);
						
						if (affichage == true) {
							
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Sortie Decodeur", 10);
							Sonde<Boolean> sondeCodage = new SondeLogique("Sonde Sortie Codage", 10);
							Sonde<Boolean> sondeDeodage = new SondeLogique("Sonde Sortie Decodage", 10);
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							decodeurAnalogique.connecter(sondeC);
							codageCanal.connecter(sondeCodage);
							decodageCanal.connecter(sondeDeodage);
						}
					}
				}
				
			} else { //si la transmission est numerique
				//connexion des differents elements du systeme
				source.connecter(codageCanal);
				codageCanal.connecter(decodageCanal);
				decodageCanal.connecter(transmetteurLogique);
				transmetteurLogique.connecter(destination);
				

				if (affichage == true) {
						Sonde<Boolean> sondeE = new SondeLogique("Sonde Entree", 10);
						Sonde<Boolean> sondeCodage = new SondeLogique("Sonde Sortie Codage", 10);
						Sonde<Boolean> sondeDeodage = new SondeLogique("Sonde Sortie Decodage", 10);
						Sonde<Boolean> sondeS = new SondeLogique("Sonde Reception", 10);
						
						
						source.connecter(sondeE);
						codageCanal.connecter(sondeCodage);
						decodageCanal.connecter(sondeDeodage);
						transmetteurLogique.connecter(sondeS);
				}
			}
			
		} else { // si la transmission n'est pas codee
			
			if (transAnalogique == true) { //si la transmission est analogique
				
				// instanciation de l'emetteur et du decodeur
				EmetteurAnalogique emetteurAnalogique = new EmetteurAnalogique(form, ne, min, max);
				DecodeurAnalogique decodeurAnalogique = new DecodeurAnalogique(form, ne, min, max);
	
				if (transBruitee == true) { // si la transmission est bruitee
					// instanciation du generateur de bruit
					
					GenerateurBruitGaussien generateurBruit = new GenerateurBruitGaussien(snr, ne);
					
					/*
					if (aleatoireAvecGerme) {
						generateurBruit = new GenerateurBruitGaussien(snr, ne, seed);
					} else {
						generateurBruit = new GenerateurBruitGaussien(snr, ne);
					} */
					
					if(transMultiTraj == true) { // si la transmission subit des decalages
						//instanciation du multi-trajet
						EmetteurMultiTrajets emetteurMT = new EmetteurMultiTrajets(listTaus, listAlphas);
						//RecepteurMultiTrajets recepteurMT = new RecepteurMultiTrajets(listTaus, listAlphas);
						
						//connexion des differents elements du systeme
						source.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(emetteurMT);
						emetteurMT.connecter(generateurBruit);
						//generateurBruit.connecter(recepteurMT);
						generateurBruit.connecter(decodeurAnalogique);
						//recepteurMT.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(destination);
						
						if (affichage == true) { //si l'on souhaite afficher les graphes
							
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Float> sondeB = new SondeAnalogique("Signal Bruite");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Sortie Decodeur", 10);
							Sonde<Float> sondeEMT = new SondeAnalogique("Signal sortie emetteur MT");
							Sonde<Float> sondeRMT = new SondeAnalogique("Signal sortie recepteur MT");
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							generateurBruit.connecter(sondeB);
							decodeurAnalogique.connecter(sondeC);
							emetteurMT.connecter(sondeEMT);
							//recepteurMT.connecter(sondeRMT);
						}
						
					} else { // si la transmission ne subit pas de decalage
						//connexion des differents elements du systeme
						source.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(generateurBruit);
						generateurBruit.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(destination);
						
						if (affichage == true) { //si l'on souhaite afficher les graphes
							
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Float> sondeB = new SondeAnalogique("Signal Bruite");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Sortie Decodeur", 10);
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							generateurBruit.connecter(sondeB);
							decodeurAnalogique.connecter(sondeC);
						}
					}
	
				} else { // si la transmission n'est pas bruitee
	
					if(transMultiTraj == true) { // si la transmission subit des decalages
						EmetteurMultiTrajets emetteurMT = new EmetteurMultiTrajets(listTaus, listAlphas);
						RecepteurMultiTrajets recepteurMT = new RecepteurMultiTrajets(listTaus, listAlphas);
						
						//connexion des differents elements du systeme
						source.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(emetteurMT);
						emetteurMT.connecter(recepteurMT);
						recepteurMT.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(destination);
						
						if (affichage == true) {
	
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Sortie Decodeur", 10);
							Sonde<Float> sondeEMT = new SondeAnalogique("Signal sortie emetteur MT");
							Sonde<Float> sondeRMT = new SondeAnalogique("Signal sortie recepteur MT");
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							decodeurAnalogique.connecter(sondeC);
							emetteurMT.connecter(sondeEMT);
							recepteurMT.connecter(sondeRMT);
						}
					} else { // si la transmission ne subit pas de decalage
						//connexion des differents elements du systeme
						source.connecter(emetteurAnalogique);
						emetteurAnalogique.connecter(decodeurAnalogique);
						decodeurAnalogique.connecter(destination);
						
						if (affichage == true) {
							
							// Creation des sondes
							Sonde<Boolean> sondeL = new SondeLogique("Signal Source", 10);
							Sonde<Float> sondeE = new SondeAnalogique("Signal Analogique Entree");
							Sonde<Boolean> sondeC = new SondeLogique("Signal Sortie Decodeur", 10);
	
							// Connexion des sondes
							source.connecter(sondeL);
							emetteurAnalogique.connecter(sondeE);
							decodeurAnalogique.connecter(sondeC);
						}
					}
				}
				
			} else { //si la transmission est numerique
				//connexion des differents elements du systeme
				source.connecter(transmetteurLogique);
				transmetteurLogique.connecter(destination);
				

				if (affichage == true) {
						Sonde<Boolean> sondeE = new SondeLogique("Sonde Entree", 10);
						Sonde<Boolean> sondeS = new SondeLogique("Sonde Sortie", 10);
						
						source.connecter(sondeE);
						transmetteurLogique.connecter(sondeS);
				}
			}
		}
	}

	/**
	 * La methode analyseArguments extrait d'un tableau de chaenes de caracteres les
	 * differentes options de la simulation. <br>
	 * Elle met e jour les attributs correspondants du Simulateur.
	 *
	 * @param args le tableau des differents arguments. <br>
	 *             <br>
	 *             Les arguments autorises sont : <br>
	 *             <dl>
	 *             <dt>-mess m</dt>
	 *             <dd>m (String) constitue de 7 ou plus digits e 0 | 1, le message
	 *             e transmettre</dd>
	 *             <dt>-mess m</dt>
	 *             <dd>m (int) constitue de 1 e 6 digits, le nombre de bits du
	 *             message "aleatoire" e transmettre</dd>
	 *             <dt>-s</dt>
	 *             <dd>pour demander l'utilisation des sondes d'affichage</dd>
	 *             <dt>-seed v</dt>
	 *             <dd>v (int) d'initialisation pour les generateurs aleatoires</dd>
	 *             </dl>
	 *
	 * @throws ArgumentsException si un des arguments est incorrect.
	 *
	 */
	public void analyseArguments(String[] args) throws ArgumentsException {

		for (int i = 0; i < args.length; i++) { // traiter les arguments 1 par 1

			if (args[i].matches("-s")) {
				affichage = true;
			}

			else if (args[i].matches("-seed")) {
				aleatoireAvecGerme = true;
				i++;
				// traiter la valeur associee
				try {
					seed = Integer.valueOf(args[i]);
				} catch (Exception e) {
					throw new ArgumentsException("Valeur du parametre -seed  invalide :" + args[i]);
				}
			}

			else if (args[i].matches("-mess")) {
				i++;
				// traiter la valeur associee
				messageString = args[i];
				if (args[i].matches("[0,1]{7,}")) { // au moins 7 digits
					messageAleatoire = false;
					nbBitsMess = args[i].length();
				} else if (args[i].matches("[0-9]{1,6}")) { // de 1 e 6 chiffres
					messageAleatoire = true;
					nbBitsMess = Integer.valueOf(args[i]);
					if (nbBitsMess < 1)
						throw new ArgumentsException("Valeur du parametre -mess invalide : " + nbBitsMess);
				} else
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
			} else if (args[i].matches("-form")) {
				transAnalogique = true;
				i++;
				try {
					form = args[i];
				} catch (Exception e) {
					throw new ArgumentsException("Valeur de la forme est invalide : " + args[i]);
				}
			} else if (args[i].matches("-snrpb")) {
				transBruitee = true;
				i++;
				try {
					snr = Float.valueOf(args[i]);
				} catch (Exception e) {
					throw new ArgumentsException("Valeur du snr invalide : " + args[i]);
				}
			} else if (args[i].matches("-ti")) {
				transMultiTraj = true;
				
				int debutTI = i;
				
				while (i+1 < args.length && !args[i+1].matches("^-[a-z]+") && i < debutTI+11)
				{					
					i++;
					try {
						listTaus.add(Integer.valueOf(args[i]));
					} catch (Exception e) {
						throw new ArgumentsException("Valeur de dt invalide : " + args[i]);
					}
					
					i++;
					if(args[i].matches("0[.][0-9]+|1"))
					{
						try {
							listAlphas.add(Float.valueOf(args[i]));
						} catch (Exception e) {
							throw new ArgumentsException("Valeur de ar invalide : " + args[i]);
						}
					}
					else
					{
						throw new ArgumentsException("Valeur de ar invalide : " + args[i]);
					}
					
				}
				
			} else if (args[i].matches("-codeur")) {
				codage = true;
			}
			
			
			// TODO : ajouter ci-apres le traitement des nouvelles options

			else
				throw new ArgumentsException("Option invalide :" + args[i]);
		}

	}

	/**
	 * La methode execute effectue un envoi de message par la source de la chaene de
	 * transmission du Simulateur.
	 *
	 * @throws Exception si un probleme survient lors de l'execution
	 *
	 */
	public void execute() throws Exception {
		source.emettre();
	}

	/**
	 * La methode qui calcule le taux d'erreur binaire en comparant les bits du
	 * message emis avec ceux du message reeu.
	 *
	 * @return La valeur du Taux dErreur Binaire.
	 */
	public float calculTauxErreurBinaire() {

		int longueurEmission = source.getInformationEmise().nbElements();
		int nbErreurs = 0;
		
		Iterator<Boolean> iteratorS = source.getInformationEmise().iterator();
		Iterator<Boolean> iteratorR = destination.getInformationRecue().iterator();
		
		while(iteratorS.hasNext() && iteratorR.hasNext())
		{
			Boolean bitEmis = iteratorS.next();
			Boolean bitRecu = iteratorR.next();
			
			if(bitEmis != bitRecu)
				nbErreurs++;
		}
		
		/*
		int longueurEmission = source.getInformationEmise().nbElements();
		int nbErreur = 0;
		int longueurSignalFinal = destination.getInformationRecue().nbElements();
		
		System.out.println("Entree : " + longueurEmission + " Sortie : " + longueurSignalFinal);
		
		Information<Boolean> informationInitiale = source.getInformationEmise();
		Information<Boolean> informationFinale = destination.getInformationRecue();
		
		System.out.println("S " + informationInitiale.nbElements() + " : " + "E " +informationFinale.nbElements());
		int pos = 0;
		for(boolean infS : informationInitiale) {
			if (infS != informationFinale.iemeElement(pos)) {
				nbErreur++;
			}
			pos++;
		}
		*/
		return ((float) nbErreurs/(float) longueurEmission);
	}

	/**
	 * La fonction main instancie un Simulateur e l'aide des arguments parametres et
	 * affiche le resultat de l'execution d'une transmission.
	 * 
	 * @param args les differents arguments qui serviront e l'instanciation du
	 *             Simulateur.
	 */
	public static void main(String[] args) {

		Simulateur simulateur = null;

		try {
			simulateur = new Simulateur(args);
		} catch (Exception e) {
			System.out.println(e);
			System.exit(-1);
		}

		try {
			simulateur.execute();
			String s = "java  Simulateur  ";
			for (int i = 0; i < args.length; i++) { // copier tous les parametres de simulation
				s += args[i] + "  ";
			}
		System.out.println(s + "  =>   TEB : " + simulateur.calculTauxErreurBinaire());
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			System.exit(-2);
		}
	}
}
