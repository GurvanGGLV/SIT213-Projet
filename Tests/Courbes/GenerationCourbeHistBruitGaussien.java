package Courbes;

import transmetteurs.EmetteurAnalogique;
import transmetteurs.GenerateurBruitGaussien;

import java.io.*;

import information.Information;
import sources.SourceFixe;

/**
 * 
 * La classe GenerationCourbeHistBruitGaussien.java permet de simuler un bruit gaussien dans notre simulateur 
 * afin d'envoyer ses valeurs dans un fichier .txt qui sera utilisé pour tracer l'histogramme de notre buit blanc gaussien centré sur octave
 * 
 * 
 *         Etudiant : Christopher, Gurvan, Aurelien, Alexandre promotion FIP2023
 *         FIP2A
 *
 */
public class GenerationCourbeHistBruitGaussien {
	
	public static void main(String[] args) throws IOException {

		final String chemin = "./GenerationCourbeHistBruitGaussien" ;  // Crée le dossier dans le répertoire parent

		final File fichier =new File(chemin); // Variable File localisé dans le chemin
		
		boolean res = fichier.mkdir(); // Crée un dossier dans le chemin 1 si ok 0 sinon

		if (res) 
			System.out.println("Le dossier a été créé.");
		else 
			System.out.println("Dossier GenerationCourbeHistBruitGaussien existant");
		
		final FileWriter writer = new FileWriter(fichier+"/GenBruit.txt"); // permet de créer un fichier qui peut être écrit

		SourceFixe source = null;
		EmetteurAnalogique emetteurAnalogique;
		GenerateurBruitGaussien bruit1;
	    Information<Float> informationAnalogique1;
		
		
		
		double [] values = new double [100000]; // Tableau pour stocker nos valeurs de bruit	
		bruit1 = new GenerateurBruitGaussien(2, 30); // Crée un bruit de snr 2 et de 30 echantillons


		emetteurAnalogique = new EmetteurAnalogique("NRZT", 50, -5, 5); //Création d'un signal de type NRZT amplitude -5 5 et avec 50 echantillons
		SourceFixe sourceFixe = new SourceFixe("00101110100011011001110101000110"); //création d'un signal à emettre de type fixe pour notre test
		sourceFixe.connecter(emetteurAnalogique); // On connecte notre sourceFixe à notre emetteur analogique
		emetteurAnalogique.connecter(bruit1); // On connecte notre emetteur au canal bruité
		informationAnalogique1 = bruit1.getInformationRecue();

		float sigma;
		try {
			sourceFixe.emettre();
			sigma = bruit1.calculSigma(); // Calcul du sigma pour notre bruit1
			float bruit; 
			for(int i = 0; i<100000; i++) // on génère 100 000 valeurs de bruit
			{
				bruit = bruit1.calculBruit(sigma,null); //on calcule le bruit pour chaque valeur en fonction du sigma calculé (null pour la seed, verifier si on peut changer ca)
				values[i] = Math.round(bruit); // On arrondi toutes nos valeurs de bruit et on les insérent dans le tableaux
				writer.write(+ values[i] + " "); // On écrit dans le writer le TEB moyenné pour les nbSignaux pour chaque niveau de bruit snrpb en Db		
			}
			// On ferme le fichier en cours d'écriture
						writer.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}}}


	

