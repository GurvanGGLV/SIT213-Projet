package Courbes;

import transmetteurs.EmetteurAnalogique;
import transmetteurs.GenerateurBruitGaussien;

import java.io.*;

import information.Information;
import sources.SourceFixe;

/**
 * 
 * La classe GenerationCourbeHistBruitGaussien.java permet de simuler un bruit gaussien dans notre simulateur 
 * afin d'envoyer ses valeurs dans un fichier .txt qui sera utilisé pour tracer l'histogramme de notre buit blanc gaussien centré
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
		
		
		
		double [] values = new double [100000];
		bruit1 = new GenerateurBruitGaussien(2, 30);


		emetteurAnalogique = new EmetteurAnalogique("NRZT", 50, -5, 5);
		SourceFixe sourceFixe = new SourceFixe("00101110100011011001110101000110");
		sourceFixe.connecter(emetteurAnalogique);
		emetteurAnalogique.connecter(bruit1);
		informationAnalogique1 = bruit1.getInformationRecue();

		float sigma;
		try {
			sourceFixe.emettre();
			sigma = bruit1.calculSigma();
			float bruit; 
			for(int i = 0; i<100000; i++)
			{
				bruit = bruit1.calculBruit(sigma);
				values[i] = Math.round(bruit*1000000)/1000000;
				writer.write(+ values[i] + " "); // On écrit dans le writer le TEB moyenné pour les nbSignaux pour chaque niveau de bruit snrpb en Db		
			}
			// On ferme le fichier en cours d'écriture
						writer.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}}}


	

