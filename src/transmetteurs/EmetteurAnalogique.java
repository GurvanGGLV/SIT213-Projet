package transmetteurs;

/**
 * Nom de classe 			: EmetteurAnalogique
 * 
 * Description 				: Cette classe se compose des methodes recevoir() et emettre() ainsi que 
 * 							  des methodes de transmission de l'information. Elle a pour but de formater l'information numerique
 * 							  en une information analogique.
 * 
 * Version 					: 1.0
 * 
 * Date 					: 19/09/2021
 * 
 * Copyright 				: Gurvan, Christopher, Alexandre, Aurelien Promotion 2023 FIP 2A
 * 
 */

import java.util.Iterator;

import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class EmetteurAnalogique extends Transmetteur<Boolean, Float> {

	private String forme;

	private int nEch;

	private float min;

	private float max;

	protected Information<Float> informationAnalogique;

	public EmetteurAnalogique(String forme, int nEch, float min, float max) {

		super();
		
		this.forme = forme;
		this.nEch = nEch;
		this.min = min;
		this.max = max;
		informationAnalogique = null;

	}

	
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {

		this.informationRecue = information;
		this.emettre();

	}

	public void emettre() throws InformationNonConformeException {

		if (forme.equalsIgnoreCase("NRZT")) {
			nRZT(nEch, max, min);
		} // continuer pour les autres encodages
		
		// DEBUG
		//int size = informationAnalogique.nbElements();
		
		if(forme.equalsIgnoreCase("NRZ")) {
			NRZ(nEch, max, min);
		}
		
		if(forme.equalsIgnoreCase("RZ")) {
			RZ(nEch, max);
		}
		
		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationAnalogique);
		}
		this.informationEmise = informationAnalogique;
	}
	
	/**
	 * La methode NRZ est un mode de transmission de l'information, elle permet 
	 * de transmettre l'information numerique en analogique par la simulation d'un signal rectangulaire.
	 * Les valeurs fixees des parametres permettent d'avoir un signal jonglant entre les negatifs <min> et les positifs <max>
	 *  
	 * @param nEch
	 * @param max
	 * @param min
	 * @throws InformationNonConformeException
	 * 
	 */
	
	public void NRZ(int nEch, float max, float min) throws InformationNonConformeException {
		
		informationAnalogique = new Information<Float>();
		
		for(int i = 0 ; i < this.informationRecue.nbElements() ; i++) { // 1
			for (int k = 0 ; k < nEch ; k++) { // 2
				
				// Les elements du message sont parcourus 1 a 1, chaque element est ensuite decompose en nEch nombre d'echantillon
				// la boucle "1" parcourt le message binaire tandis que la boucle "2" recompose l'information logique en une multitude de points => approximation du signal analogique
			
				if(this.informationRecue.iemeElement(i)) {
					informationAnalogique.add(max); 
				}
				else informationAnalogique.add(min); 
			}
		}	
	}
	
	/**
	 * La methode RZ est un mode de transmission de l'information, elle permet de transmettre
	 * l'information numerique en analogique par la simulation d'un signal rectangulaire qui 
	 * ne prendra ses valeurs qu'entre <max> et <0>.
	 * 
	 * @param nEch
	 * @param max
	 * @throws InformationNonConformeException
	 * 
	 */
	
	public void RZ(int nEch, float max) throws InformationNonConformeException {
		
		informationAnalogique = new Information<Float>();
		
		float dist = (float)nEch/(float)3;
	
		for(boolean bitCourant : informationRecue) { // on regarde chaque bit
			if(bitCourant == true) { // si c'est un true
				for(int j=0 ; j<dist ; j++) { // 1/3 tbit ?? 0
					informationAnalogique.add(0f);
				}
				for(int j=0 ; j<dist ; j++) { // 1/3 tbit ?? max
					informationAnalogique.add(max);
				}
				for(int j=0 ; j<dist ; j++) { // 1/3 tbit ?? 0
					informationAnalogique.add(0f);
				}
			} else { // sinon tout ?? 0
				for(int tBit=0 ; tBit<3 ; tBit++) {
					for(int j=0 ; j<dist ; j++) {
						informationAnalogique.add(0f);
					}
				}
			}
		}
	}
	
	/**
	 * La methode nRZT est un mode de transmission de l'information, elle permet de transmettre
	 * l'information numerique en analogique par la simulation d'un signal rectangulaire qui 
	 * ne prendra ses valeurs qu'entre <max> et <min> cependant, contrairement a la methode NRT, 
	 * cette methode n'aura pas d'echelon lors des changements de valeurs. La transition entre la valeur <max> et <min>
	 * se fera par un pas incremental.
	 * 
	 * @param nEch
	 * @param max
	 * @param min
	 * @throws InformationNonConformeException
	 * 
	 */
	
	public void nRZT(int nEch, float max, float min) throws InformationNonConformeException {

		informationAnalogique = new Information<Float>();

		// d??finition des valeurs de pas
		float pasP = calculPasPositif(nEch, max);
		float pasN = calculPasNegatif(nEch, min);
		double dist = (float)nEch / 3.0f;

		Iterator<Boolean> bitCourant = informationRecue.iterator();

		// codage avec for each
		int position = 0; // sert ?? savoir o?? l'on est dans informationRecue
		for (boolean b : informationRecue) {
			// on prend le cas o?? il n'y a qu'un bit
			if (informationRecue.nbElements() == 1) {
				// on regarde la valeur du bit
				if (b == true) {
					float init = 0;
					for (int j = 0; j < dist; j++) {
						informationAnalogique.add(init);
						System.out.println(init);
						init += pasP;
					}
					for (int j = 0; j < dist; j++) {
						informationAnalogique.add(max);
					}
					for (int j = 0; j < dist; j++) {
						init -= pasP;
						informationAnalogique.add(init);
					}
				} else if (b == false) {
					float init = 0;
					for (int j = 0; j < dist; j++) {
						informationAnalogique.add(init);
						init += pasN;
					}
					for (int j = 0; j < dist; j++) {
						informationAnalogique.add(min);
					}
					for (int j = 0; j < dist; j++) {
						init -= pasN;
						informationAnalogique.add(init);
					}
				}
			}

			// sinon on traite diff??remment la liste
			else {
				int bitPrec = position - 1;
				if (position == 0) { // on regarde pour la premi??re position
					if (b == true) { // r??ception d'un bit positif
						float init = 0;
						for (int j = 0; j < dist; j++) { // mont??e
							informationAnalogique.add(init);
							init += pasP;
						}
						for (int j = 0; j < dist; j++) { // valeur max 1/3 tbit
							informationAnalogique.add(max);
						}
					} else if (b == false) { // r??ception d'un bit n??gatif
						float init = 0;
						for (int j = 0; j < dist; j++) { // on passe de 0 ?? min
							informationAnalogique.add(init);
							init += pasN;
						}
						for (int j = 0; j < dist; j++) { // plafond ?? min
							informationAnalogique.add(init);
						}
					}
				} else if (position != 0) { // si on est entre le d??but et la fin (-1?)
					if (b == true) { // on re??oit un bit positif
						if (informationRecue.iemeElement(bitPrec) == b) { // si le bit
																										// d'avant ??tait
																										// positif
																										// ??galement
							for (int j = 0; j < dist; j++) { // on laisse 3 paliers de max
								informationAnalogique.add(max);
							}
							for (int j = 0; j < dist; j++) { // 2/3
								informationAnalogique.add(max);
							}
							for (int j = 0; j < dist; j++) { // 3/3
								informationAnalogique.add(max);
							}
						} else if (informationRecue.iemeElement(bitPrec) != b) { // si le bit pr??c??dent ??tait
																						// n??gatif
							float init = min; // on se place ?? min
							for (int j = 0; j < dist; j++) { // on remonte la pente sur 1/3 tBit
								init -= pasN;
								informationAnalogique.add(init);
							}
							init = 0; // on se place ?? 0
							for (int j = 0; j < dist; j++) { // on monte jusqu'?? max sur 1/3 tBit
								init += pasP;
								informationAnalogique.add(init);
							}
							for (int j = 0; j < dist; j++) { // on reste 1/3 tBit ?? max
								informationAnalogique.add(max);
							}
						}
					} else if (b == false) { // si l'on re??oit un bit 0
						if (informationRecue.iemeElement(bitPrec) == b) { // et que le pr??c??dent ??tait 0 aussi
							for (int j = 0; j < (dist); j++) { // on reste ?? min 1/3
								informationAnalogique.add(min);
							}
							for (int j = 0; j < dist; j++) { // 2/3
								informationAnalogique.add(min);
							}
							for (int j = 0; j < dist; j++) { // 3/3
								informationAnalogique.add(min);
							}
						} else if (informationRecue.iemeElement(bitPrec) != b) { // si le pr??c??dent ??tait un 1
							float init = max;
							for (int j = 0; j < dist; j++) { // on redescend jusq'?? 0
								informationAnalogique.add(init);
								init -= pasP;
							}
							// init=0; // on se place bien ?? 0
							for (int j = 0; j < dist; j++) { // on descend jusqu'?? min 1/3 tBit
								init += pasN;
								informationAnalogique.add(init);
							}
							for (int j = 0; j < dist; j++) {
								informationAnalogique.add(init); // on reste ensuite ?? min pendant 1/3 tBit
							}
						}
					}
				} 
			}
			position++;
			if (position == informationRecue.nbElements()) { // quand on est au dernier ??l??ment
				if (informationRecue.iemeElement(position-1) == true) { // on regarde si on ??tait ?? 1
					// alors on redescend jusqu'?? 0
					float init = max;
					for (int j=0 ; j<dist ; j++) {
						init-=pasP;
						informationAnalogique.add(init);
					}
				} else { // sinon on ??tait ?? 0
					// et il faut remonter
					float init = min;
					for (int j=0 ; j<dist ; j++) {
						init-=pasN;
						informationAnalogique.add(init);
					}
				}	
			}
		}
	}
	

	/**
	 * Cette m??thode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param nEch - nombre d'??chantillons du signal
	 * @return - un float ??tant la valeur du pas
	 */
	public float calculPasPositif(int nEch, float max) {

		float xA = 0;
		float xB = (float) nEch / 3;
		float yA = 0;
		float yB = max;

		float pas = (yB - yA) / (xB - xA);

		return pas;
	}

	/**
	 * Cette m??thode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param nEch - nombre d'??chantillons du signal
	 * @return - un float ??tant la valeur du pas
	 */
	public float calculPasNegatif(int nEch, float min) {

		float xA = 0;
		float xB = (float) nEch / 3;
		float yA = 0;
		float yB = min;

		float pas = (yB - yA) / (xB - xA);

		return pas;
	}

	public int getnEch() {
		return nEch;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

}