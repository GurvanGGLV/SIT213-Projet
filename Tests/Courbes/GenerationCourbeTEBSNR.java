package Courbes;


import simulateur.Simulateur;
import java.io.*;
import java.text.*;

/**
 * 
 * La classe GenerationCourbeTEBSNR.java permet de simuler un grand nombre de réalisations de notre simulateur 
 * afin de pouvoir génerer des valeurs utiles de TEB dans des fichiers .txt qui seront utilisés pour 
 * tracer les courbes de rapport SNR en fonction du TEB, pour les trois codages : NRZ, NRZT, RZ
 * 
 * 
 *         Etudiant : Christopher, Gurvan, Aurelien, Alexandre promotion FIP2023
 *         FIP2A
 *
 */
public class GenerationCourbeTEBSNR {

	public static void main(String[] args) throws IOException {

		final String chemin = "./GenerationCourbeTESBSNR" ;  // Crée le dossier dans le répertoire parent

		final File fichiers =new File(chemin); // Variable File localisé dans le chemin

		boolean res = fichiers.mkdir(); // Crée un dossier dans le chemin 1 si ok 0 sinon

		if(res) 
			System.out.println("Le dossier a été créé.");
		else 
			System.out.println("Dossier GenerationCourbeTESBSNR existant");

		Simulateur simulateur = null; // Création du simulateur 
		int nbSignaux = 5; // Nombre de signaux à simuler pour l'exécution
		float snrDb =0; //  Initialisation de la variable snrDb utiliser pour récuper la valeur logarithmique de snrpb
		String[] codages = new String[] {"RZ", "NRZ", "NRZT"}; // Initialise et instancie les trois types de codages

		// Boucle pour tester les trois codages
		for (int indexCodages = 0; indexCodages<3 ; indexCodages++){
			String typeCodage = codages[indexCodages]; // Prend la valeur des trois codage en fonction de la valeur de l'indexCodages
			System.out.println(codages[indexCodages]);
			
			final FileWriter writer = new FileWriter(fichiers+"/Gen"+typeCodage+".txt"); // permet de créer un fichier qui peut être écrit
			
			float indexSNR = 10; // Initialise le snrpb à 10 à chaque itération pour les trois codages

			
			// Boucle pour le test en fonction des différents niveaux de bruit
			while (indexSNR > -40 ) {
				// Création de différents pas de décrémentation pour obtenir le plus de valeurs pertinentes
				if (indexSNR > 4) {
					indexSNR--;
				}
				
				if (indexSNR > -9 && indexSNR <= 4  ) {
					indexSNR = (float) (indexSNR - 0.25);	

					// Cas ou SNR = 0 pour éviter d'avoir un SNRdB non défini (infini)
					if (indexSNR == 0);{
						indexSNR = indexSNR + (float) 0.0001; 
					}
				}
				if (indexSNR <-9 ) {
					indexSNR--;
				}

				// On convertit la valeur du snrpb en String pour pouvoir la passer en argument du simulateur
				String indexSNRString =	 String.valueOf(indexSNR);
				// Arguments passés dans le simulateur
				String[] arg = new String[]{"-mess","2500" , "-form",  typeCodage ,  "-ampl", "-2",  "2",  "-nbEch", "30" , "-snrpb" , indexSNRString };

				// Initialisation du simulateur avec les bon arguments
				try {

					simulateur = new Simulateur(arg);

				} catch (Exception e) {
					System.out.println(e);
					System.exit(-1);
				}

				try {
					String s = "java  Simulateur  ";
					for (int i = 0; i < arg.length; i++) { // copier tous les param�tres de simulation
						s += arg[i] + "  ";
					}

					float totTEB = 0; // Variable pour l'addition des TEB
					
					System.out.println(s + " Pour " + nbSignaux + " signaux. " );
					
					// On génère nbSignaux dans le simulateur
					for (int j = 0 ; j<nbSignaux ; j++) { 
						simulateur.execute();
						System.out.println("Signal n° " + (j+1) +"  =>   TEB : " + simulateur.calculTauxErreurBinaire());
						totTEB += simulateur.calculTauxErreurBinaire(); // On additionne les TEB des signaux 
					}

					// Modification sur la forme du SNR
					float snrLineaire = Float.parseFloat(arg[10]); // Converti la string associée au SNR linéraire en float

					if (snrLineaire <0 ) // En fonction de si le snr est négatif :
						snrDb = (float) (-10*Math.log10(-snrLineaire)); //Soit inverse le SNR Db pour obtenir le SNR négatif
					else	
						snrDb = (float) (10*Math.log10(snrLineaire)); // Soit on calcul le SNR Db normalement

					// On utilise les méthodes de NumberFormat pour limiter le nombre de chiffre après la virgule
					NumberFormat format=NumberFormat.getInstance(); // Initialise une variable format pour pouvoir utiliser les méthodes de la classe NumberFormat
					format.setMinimumFractionDigits(3); // limite à trois chiffres après la virgule
					String snrDbString =format.format(snrDb); // On reconverti notre snrDb en String pour l'affichage dans les fichiers .txt


					// Ecriture des résultats dans les fichiers 
					writer.write(+ totTEB/nbSignaux + " " + snrDbString + "\n"); // On écrit dans le writer le TEB moyenné pour les nbSignaux pour chaque niveau de bruit snrpb en Db		



				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
					System.exit(-2);
				}
			}
			// On ferme le fichier en cours d'écriture
			writer.close();	
		}
	}
}

