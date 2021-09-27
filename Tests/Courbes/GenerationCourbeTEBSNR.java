package Courbes;


import simulateur.Simulateur;

import java.io.*;

public class GenerationCourbeTEBSNR {

	public static void main(String[] args) throws IOException {
		
		    final String chemin = "C:\\Users\\Alexandre Guinet\\Downloads\\test.txt";
		    final File fichier =new File(chemin);
		    final FileWriter writer = new FileWriter(fichier);

		
			Simulateur simulateur = null;
			int nbSignaux = 5;
			//String[] arg = new String[]{"-mess","99", "-s" , "-form",  "NRZ",  "-ampl", "-1",  "2",  "-nbEch", "100"};
			String[] arg = new String[]{"-mess","99999" , "-form",  "NRZ",  "-ampl", "-1",  "2",  "-nbEch", "10" , "-snrpb","-30"};

			try {
			    fichier.createNewFile();

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
				System.out.println(s + " Pour " + nbSignaux + " signaux. " );
				for (int j = 0 ; j<nbSignaux ; j++) { 
				simulateur.execute();
				System.out.println("Signal n° " + (j+1) +"  =>   TEB : " + simulateur.calculTauxErreurBinaire());
				writer.write(+ simulateur.calculTauxErreurBinaire()+" ");
				}

	                // quoiqu'il arrive, on ferme le fichier
	                writer.close();
		
				
							
				

			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				System.exit(-2);
			}
		}
	}
