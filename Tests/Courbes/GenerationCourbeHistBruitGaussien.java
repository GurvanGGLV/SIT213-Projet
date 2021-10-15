package Courbes;

import transmetteurs.EmetteurAnalogique;
import transmetteurs.GenerateurBruitGaussien;

import java.io.*;

import information.Information;
import sources.SourceFixe;

/**
 * 
 * La classe GenerationCourbeHistBruitGaussien.java permet de simuler un bruit gaussien dans notre simulateur 
 * afin d'envoyer ses valeurs dans un fichier .txt qui sera utilise pour tracer l'histogramme de notre buit blanc gaussien centre sur octave
 * 
 * 
 *         Etudiants : Christopher, Gurvan, Aurelien, Alexandre promotion FIP2023
 *         FIP2A
 *
 */
public class GenerationCourbeHistBruitGaussien {
	
	public static void main(String[] args) throws IOException {

		final String chemin = "./GenerationCourbeHistBruitGaussien" ;  // Cree le dossier dans le repertoire parent

		final File fichier =new File(chemin); // Variable File localise dans le chemin
		
		boolean res = fichier.mkdir(); // Cree un dossier dans le chemin 1 si ok 0 sinon

		if (res) 
			System.out.println("Le dossier a ete cree.");
		else 
			System.out.println("Dossier GenerationCourbeHistBruitGaussien existant");
		
		final FileWriter writer = new FileWriter(fichier+"/GenBruit.txt"); // permet de creer un fichier qui peut etre ecrit

		SourceFixe source = null;
		EmetteurAnalogique emetteurAnalogique;
		GenerateurBruitGaussien bruit1;
	    Information<Float> informationAnalogique1;
		
		
		
		double [] values = new double [100000]; // Tableau pour stocker nos valeurs de bruit	
		bruit1 = new GenerateurBruitGaussien(2, 30); // Cree un bruit de snr 2 et de 30 echantillons


		emetteurAnalogique = new EmetteurAnalogique("NRZT", 50, -5, 5); //Creation d'un signal de type NRZT amplitude -5 5 et avec 50 echantillons
		SourceFixe sourceFixe = new SourceFixe("00101110100011011001110101000110"); //creation d'un signal a emettre de type fixe pour notre test
		sourceFixe.connecter(emetteurAnalogique); // On connecte notre sourceFixe a notre emetteur analogique
		emetteurAnalogique.connecter(bruit1); // On connecte notre emetteur au canal bruite
		informationAnalogique1 = bruit1.getInformationRecue();

		float sigma;
		try {
			sourceFixe.emettre();
			sigma = bruit1.calculSigma(); // Calcul du sigma pour notre bruit1
			float bruit; 
			for(int i = 0; i<100000; i++) // on genere 100 000 valeurs de bruit
			{
				bruit = bruit1.calculBruit(sigma); //on calcule le bruit pour chaque valeur en fonction du sigma calcule (null pour la seed, verifier si on peut changer ca)
				values[i] = Math.round(bruit); // On arrondi toutes nos valeurs de bruit et on les inserent dans le tableaux
				writer.write(+ values[i] + " "); // On ecrit dans le writer le TEB moyenne pour les nbSignaux pour chaque niveau de bruit snrpb en Db		
			}
			// On ferme le fichier en cours d'ecriture
						writer.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}}}


	

