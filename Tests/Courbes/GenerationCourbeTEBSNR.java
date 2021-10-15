package Courbes;


import simulateur.Simulateur;
import java.io.*;

/**
 * 
 * La classe GenerationCourbeTEBSNR.java permet de simuler un grand nombre de realisations de notre simulateur 
 * afin de pouvoir generer des valeurs utiles de TEB dans des fichiers .txt qui seront utilises pour 
 * tracer les courbes de rapport SNR en fonction du TEB, pour les trois codages : NRZ, NRZT, RZ
 * 
 * 
 *         Etudiant : Christopher, Gurvan, Aurelien, Alexandre promotion FIP2023
 *         FIP2A
 *
 */
public class GenerationCourbeTEBSNR {

	public static void main(String[] args) throws IOException {

		final String chemin = "./GenerationCourbeTESBSNR" ;  // Cree le dossier dans le repertoire parent

		final File fichiers =new File(chemin); // Variable File localise dans le chemin

		boolean res = fichiers.mkdir(); // Cree un dossier dans le chemin 1 si ok 0 sinon

		if(res) 
			System.out.println("Le dossier a ete cree.");
		else 
			System.out.println("Dossier GenerationCourbeTESBSNR existant");

		Simulateur simulateur = null; // Creation du simulateur 
		int nbSignaux = 3; // Nombre de signaux a simuler pour l'execution
		String[] codages = new String[] {"NRZ", "RZ", "NRZT"}; // Initialise et instancie les trois types de codages

		// Boucle pour tester les trois codages
		for (int indexCodages = 0; indexCodages<3 ; indexCodages++){
			String typeCodage = codages[indexCodages]; // Prend la valeur des trois codage en fonction de la valeur de l'indexCodages
			System.out.println("Generation des valeurs pour " + codages[indexCodages]);
			
			final FileWriter writer = new FileWriter(fichiers+"/Gen"+typeCodage+".txt"); // permet de creer un fichier qui peut etre ecrit
			
			float indexSNR = 10; // Initialise le snrpb a 10 a chaque iteration pour les trois codages

			
			// Boucle pour le test en fonction des differents niveaux de bruit
			while (indexSNR > -40 ) {
				// Creation de differents pas de decrementation pour obtenir le plus de valeurs pertinentes
				if (indexSNR > 4) 
					indexSNR--;
				
				
				if (indexSNR > -9 && indexSNR <= 4  ) 
					indexSNR = (float) (indexSNR - 0.25);	
				
				
				if (indexSNR <=-9 ) {
					indexSNR--;
				}
				
				
				// On convertit la valeur du snrpb en String pour pouvoir la passer en argument du simulateur
				String indexSNRString =	 String.valueOf(indexSNR);
				// Arguments passes dans le simulateur
				String[] arg = new String[]{"-mess","3000" , "-form",  typeCodage ,  "-ampl", "-2",  "2",  "-nbEch", "11" , "-snrpb" , indexSNRString };

				// Initialisation du simulateur avec les bon arguments
				try {

					simulateur = new Simulateur(arg);

				} catch (Exception e) {
					System.out.println(e);
					System.exit(-1);
				}

				try {
					
					float totTEB = 0; // Variable pour l'addition des TEB
					
					//System.out.println(" Pour " + nbSignaux + " signaux. " ); // print test
					
					// On genere nbSignaux dans le simulateur
					for (int j = 0 ; j<nbSignaux ; j++) { 
						simulateur.execute();
						//System.out.println("Signal n° " + (j+1) +"  =>   TEB : " + simulateur.calculTauxErreurBinaire()); // print test
						totTEB += simulateur.calculTauxErreurBinaire(); // On additionne les TEB des signaux 
					}

					// Modification sur la forme du SNR
					float snrLineaire = Float.parseFloat(arg[10]); // Converti la string associee au SNR lineraire en float
					
					// Ecriture des resultats dans les fichiers 
					writer.write(+ totTEB/nbSignaux + " " + snrLineaire + "\n"); // On ecrit dans le writer le TEB moyenne pour les nbSignaux pour chaque niveau de bruit snrpb en Db		



				} catch (Exception e) {
					System.out.println(e);
					e.printStackTrace();
					System.exit(-2);
				}
			}
			// On ferme le fichier en cours d'ecriture
			writer.close();	
		}
	}}


