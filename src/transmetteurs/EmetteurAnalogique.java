package transmetteurs;

/**
 * Nom de classe 			: EmetteurAnalogique
 * 
 * Description 				: Cette classe se compose des methodes recevoir() et emettre() ainsi que 
 * 							  des methodes de transmission de l'information. Elle a pour but de formater l'information numerique
 * 							  en une information analogique.
 * 
 * Version					: 1.0
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

		// super();

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

		if (forme.equalsIgnoreCase("NRZ")) {
			NRZ(nEch, max, min);
		}

		if (forme.equalsIgnoreCase("RZ")) {
			RZ(nEch, max);
		}

		for (DestinationInterface<Float> destinationConnectee : destinationsConnectees) {
			destinationConnectee.recevoir(informationAnalogique);
		}
		this.informationEmise = informationAnalogique;
	}

	/**
	 * La methode NRZ est un mode de transmission de l'information, elle permet de
	 * transmettre l'information numerique en analogique par la simulation d'un
	 * signal rectangulaire. Les valeurs fixees des parametres permettent d'avoir un
	 * signal jonglant entre les negatifs <min> et les positifs <max>
	 * 
	 * @param nEch
	 * @param max
	 * @param min
	 * @throws InformationNonConformeException
	 * 
	 */
	public void NRZ(int nEch, float max, float min) throws InformationNonConformeException {

		informationAnalogique = new Information<Float>();

		for (boolean bitCurrent : informationRecue) { // 1
			for (int k = 0; k < nEch; k++) { // 2

				// Les elements du message sont parcourus 1 a 1, chaque element est ensuite
				// decompose en nEch nombre d'echantillon
				// la boucle "1" parcourt le message binaire tandis que la boucle "2" recompose
				// l'information logique en une multitude de points => approximation du signal
				// analogique

				if (bitCurrent) {
					informationAnalogique.add(max);
				} else
					informationAnalogique.add(min);
			}
		}
	}

	/**
	 * La methode RZ est un mode de transmission de l'information, elle permet de
	 * transmettre l'information numerique en analogique par la simulation d'un
	 * signal rectangulaire qui ne prendra ses valeurs qu'entre <max> et <0>.
	 * 
	 * @param nEch
	 * @param max
	 * @throws InformationNonConformeException
	 * 
	 */
	public void RZ(int nEch, float max) throws InformationNonConformeException {

		informationAnalogique = new Information<Float>();

		float pasP;
		float pasN;

		int tVariation;
		int tMax;

		int modulo = nEch % 3; // on verifie si c'est un multiple de 3 avant d'appliquer les operations

		float dist = (float) nEch / 3f;

		if (modulo == 0) { // si nEch est un multiple de 3, alors tous les tBits sont egaux
			tVariation = (int) dist; // alors on converti en int et on assigne
			tMax = (int) dist;
			pasP = calculPasPositif(tVariation, max);
		} else {
			int rounded = Math.round(dist); // l'operation qui vient change suivant la valeur de rounded (si elle est <
											// ou > à dist)
			if (rounded < dist) {
				tVariation = rounded;
				tMax = rounded + 1;
				pasP = calculPasPositif(tVariation, max);
			} else {
				tVariation = rounded;
				tMax = rounded - 1;
				pasP = calculPasPositif(tVariation, max);
			}
		}

		for (boolean bitCourant : informationRecue) { // on regarde chaque bit
			if (bitCourant == true) { // si c'est un true
				for (int j = 0; j < tVariation; j++) { // 1/3 tbit à 0
					informationAnalogique.add(0f);
				}
				for (int j = 0; j < tMax; j++) { // 1/3 tbit à max
					informationAnalogique.add(max);
				}
				for (int j = 0; j < tVariation; j++) { // 1/3 tbit à 0
					informationAnalogique.add(0f);
				}
			} else { // sinon tout à 0
				for (int j = 0; j < nEch; j++) {
					informationAnalogique.add(0f);
				}
			}
		}
	}

	/**
	 * La methode nRZT est un mode de transmission de l'information, elle permet de
	 * transmettre l'information numerique en analogique par la simulation d'un
	 * signal rectangulaire qui ne prendra ses valeurs qu'entre <max> et <min>
	 * cependant, contrairement a la methode NRT, cette methode n'aura pas d'echelon
	 * lors des changements de valeurs. La transition entre la valeur <max> et <min>
	 * se fera par un pas incremental.
	 * 
	 * @param nEch
	 * @param max
	 * @param min
	 * @throws InformationNonConformeException
	 * 
	 */
	public void nRZT(int nEch, float max, float min) throws InformationNonConformeException {

		// IL FAUT PASSER EN INT QUAND ON EST PAS UN MULTIPLE DE 3.
		// EX : 50/3 = 16,67 alors tMonte = 17 tMax = 16
		// EX2 : 70/3 = 23,3 alors tMonte = 23 tMax = 24
		// si la premiere decimale est en dessous de 5 ou au dessus on change
		// l'operation

		informationAnalogique = new Information<Float>();

		// definition des valeurs de pas
		float pasP;
		float pasN;

		int tVariation = 0;
		int tMax = 0;

		int modulo = nEch % 3; // on verifie si c'est un multiple de 3 avant d'appliquer les operations

		float dist = (float) nEch / 3f;

		if (modulo == 0) { // si nEch est un multiple de 3, alors tous les tBits sont egaux
			tVariation = (int) dist; // alors on converti en int et on assigne
			tMax = (int) dist;
			pasP = calculPasPositif(tVariation, max);
			pasN = calculPasPositif(tVariation, min);
		} else {
			int rounded = Math.round(dist); // l'operation qui vient change suivant la valeur de rounded (si elle est <
											// ou > à dist)
			if (rounded < dist) {
				tVariation = rounded;
				tMax = rounded + 1;
				pasP = calculPasPositif(tVariation, max);
				pasN = calculPasNegatif(tVariation, min);
			} else {
				tVariation = rounded;
				tMax = rounded - 1;
				pasP = calculPasPositif(tVariation, max);
				pasN = calculPasNegatif(tVariation, min);
			}
		}

		int position = 0; // sert a savoir ou l'on est dans informationRecue
		for (boolean b : informationRecue) {
			// on prend le cas ou il n'y a qu'un bit
			if (informationRecue.nbElements() == 1) {
				// on regarde la valeur du bit
				if (b == true) {
					float init = 0;
					for (int j = 0; j < tVariation; j++) { // front montant
						informationAnalogique.add(init);
						init += pasP;
					}
					for (int j = 0; j < tMax; j++) { // valeur max sur 1/3 tBit
						informationAnalogique.add(max);
					}
					for (int j = 0; j < tVariation; j++) { // front descendant
						init -= pasP;
						informationAnalogique.add(init);
					}
				} else if (b == false) { // on fait dans l'autre sens si c'est un false
					float init = 0;
					for (int j = 0; j < tVariation; j++) {
						informationAnalogique.add(init);
						init += pasN;
					}
					for (int j = 0; j < tMax; j++) {
						informationAnalogique.add(min);
					}
					for (int j = 0; j < tVariation; j++) {
						init -= pasN;
						informationAnalogique.add(init);
					}
				}
			}
			// sinon on traite differemment la liste
			else {
				int bitPrec = position - 1;
				if (position == 0) { // on regarde pour la premiere position
					if (b == true) { // reception d'un bit positif
						float init = 0;
						for (int j = 0; j < tVariation; j++) { // montee
							init += pasP;
							informationAnalogique.add(init);
						}
						for (int j = 0; j < tMax; j++) { // valeur max 1/3 tbit
							informationAnalogique.add(max);
						}
					} else if (b == false) { // reception d'un bit negatif
						float init = 0;
						for (int j = 0; j < tVariation; j++) { // on passe de 0 a min
							init += pasN;
							informationAnalogique.add(init);
						}
						for (int j = 0; j < tMax; j++) { // plafond a min
							informationAnalogique.add(init);
						}
					}
				} else if (position != 0) { // si on est entre le debut et la fin
					if (b == true) { // on reçoit un bit positif
						if (informationRecue.iemeElement(bitPrec) == b) { // si le bit d'avant etait positif egalement
							for (int j = 0; j < tVariation; j++) { // on laisse 3 paliers de max
								informationAnalogique.add(max);
							}
							for (int j = 0; j < tVariation; j++) { // 2/3
								informationAnalogique.add(max);
							}
							for (int j = 0; j < tMax; j++) { // 3/3
								informationAnalogique.add(max);
							}
						} else if (informationRecue.iemeElement(bitPrec) != b) { // si le bit precedent etait
																					// negatif
							float init = min; // on se place à min
							for (int j = 0; j < tVariation; j++) { // on remonte la pente sur 1/3 tBit
								init -= pasN;
								informationAnalogique.add(init);
							}
							init = 0; // on se place a 0
							for (int j = 0; j < tVariation; j++) { // on monte jusqu'a max sur 1/3 tBit
								init += pasP;
								informationAnalogique.add(init);
							}
							for (int j = 0; j < tMax; j++) { // on reste 1/3 tBit à max
								informationAnalogique.add(max);
							}
						}
					} else if (b == false) { // si l'on reçoit un bit 0
						if (informationRecue.iemeElement(bitPrec) == b) { // et que le precedent etait 0 aussi
							for (int j = 0; j < tVariation; j++) { // on reste a min 1/3
								informationAnalogique.add(min);
							}
							for (int j = 0; j < tVariation; j++) { // 2/3
								informationAnalogique.add(min);
							}
							for (int j = 0; j < tMax; j++) { // 3/3
								informationAnalogique.add(min);
							}
						} else if (informationRecue.iemeElement(bitPrec) != b) { // si le precedent etait un 1
							float init = max;
							for (int j = 0; j < tVariation; j++) { // on redescend jusqu'a 0
								init -= pasP;
								informationAnalogique.add(init);

							}
							// init=0; // on se place bien à 0
							for (int j = 0; j < tVariation; j++) { // on descend jusqu'a min 1/3 tBit
								init += pasN;
								informationAnalogique.add(init);
							}
							for (int j = 0; j < tMax; j++) {
								informationAnalogique.add(init); // on reste ensuite a min pendant 1/3 tBit
							}
						}
					}
				}
			}
			position++;
			if (position == informationRecue.nbElements()) { // quand on est au dernier element
				if (informationRecue.iemeElement(position - 1) == true) { // on regarde si on etait à 1
					// alors on redescend jusqu'a 0
					float init = max;
					for (int j = 0; j < tVariation; j++) {
						init -= pasP;
						informationAnalogique.add(init);
					}
				} else { // sinon on etait a 0
					// et il faut remonter
					float init = min;
					for (int j = 0; j < tVariation; j++) {
						init -= pasN;
						informationAnalogique.add(init);
					}
				}
			}
		}
	}

	/**
	 * Cette methode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param tVar - nombre d'echantillons du tier tBit montant
	 * @return - un float etant la valeur du pas
	 */
	public float calculPasPositif(int tVar, float max) {

		return max / (float) tVar;
	}

	/**
	 * Cette methode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param tVar - nombre d'echantillons du tier tBit montant
	 * @return - un float etant la valeur du pas
	 */
	public float calculPasNegatif(int tVar, float min) {

		return min / (float) tVar;
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