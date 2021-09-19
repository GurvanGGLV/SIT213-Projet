package transmetteurs;

// bonjour test 1

// bonjour je suis le test 2

// bonjour, je suis le test d'Aurélien

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

		this.forme = forme;
		this.nEch = nEch;
		this.min = min;
		this.max = max;
		informationAnalogique = null;

	}

	@Override
	public void recevoir(Information<Boolean> information) throws InformationNonConformeException {

		this.informationRecue = information;
		this.emettre();

	}

	@Override
	public void emettre() throws InformationNonConformeException {

		if (forme.equalsIgnoreCase("NRZT")) {
			nRZT(nEch, max, min);
		} // continuer pour les autres encodages

		
		if(forme.equalsIgnoreCase("NRZ")) {
			
		}
		
		if(forme.equalsIgnoreCase("RZ")) {
			
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
		
		for(int i = 0 ; i < this.informationRecue.nbElements() ; i++) { 
			for (int k = 0 ; k < nEch/3; k++) { 
				if(this.informationRecue.iemeElement(i)) {
					informationAnalogique.add((float) 0); 
				}
			}
			for (int j = 0 ; j > nEch/3  ; j++) {
				if(this.informationRecue.iemeElement(i)) {
					informationAnalogique.add(max); 
				}
			}
			for (int l = 0 ; l < nEch/3; l++) { 
				if(this.informationRecue.iemeElement(i)) { 
					informationAnalogique.add((float) 0);
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

		// définition des valeurs de pas
		float pasP = calculPasPositif(nEch, max);
		float pasN = calculPasNegatif(nEch, min);
		float dist = nEch / 3;

		Iterator<Boolean> bitCourant = informationRecue.iterator();

		for (int i = 0; i < informationRecue.nbElements(); i++) {

			// on prend le cas où il n'y a qu'un bit
			if (informationRecue.nbElements() == 1) {
				// on regarde la valeur du bit
				if (bitCourant.next() == true) {
					float init = 0;
					for (int j = 0; j < dist; j++) {
						informationAnalogique.add(init);
						init += pasP;
					}
					for (int j = 0; j < dist; j++) {
						informationAnalogique.add(max);
					}
					for (int j = 0; j < dist; j++) {
						init -= pasP;
						informationAnalogique.add(init);
					}
				} else if (bitCourant.next() == false) {
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

			// sinon on traite différemment la liste
			else {
				int bitPrec = i - 1;
				if (i == 0) {
					if (informationRecue.iemeElement(i)) { // réception d'un bit positif
						float init = 0;
						for (int j = 0; j < dist; j++) { // montée
							informationAnalogique.add(init);
							init += pasP;
						}
						for (int j = 0; j < dist; j++) { // valeur max 1/3 tbit
							informationAnalogique.add(max);
						}

					} else if (informationRecue.iemeElement(i) == false) { // réception d'un bit négatif
						float init = 0;
						for (int j = 0; j < dist; j++) { // on passe de 0 à min
							informationAnalogique.add(init);
							init += pasN;
						}
						for (int j = 0; j < dist; j++) { // plafond à min
							informationAnalogique.add(init);
						}
					}
				} else if (i != 0) { // si on est entre le début et la fin (-1?)
					if (informationRecue.iemeElement(i)) { // on reçoit un bit positif
						if (informationRecue.iemeElement(bitPrec) == informationRecue.iemeElement(i)) { // si le bit
																										// d'avant était
																										// positif
																										// également
							for (int j = 0; j < dist; j++) { // on laisse 3 paliers de max
								informationAnalogique.add(max);
							}
							for (int j = 0; j < dist; j++) { // 2/3
								informationAnalogique.add(max);
							}
							for (int j = 0; j < dist; j++) { // 3/3
								informationAnalogique.add(max);
							}
						} else if (informationRecue.iemeElement(bitPrec) == false) { // si le bit précédent était
																						// négatif
							float init = min; // on se place à min
							for (int j = 0; j < dist; j++) { // on remonte la pente sur 1/3 tBit
								init -= pasN;
								informationAnalogique.add(init);
							}
							init = 0; // on se place à 0
							for (int j = 0; j < dist; j++) { // on monte jusqu'à max sur 1/3 tBit
								init += pasP;
								informationAnalogique.add(init);
							}
							for (int j = 0; j < dist; j++) { // on reste 1/3 tBit à max
								informationAnalogique.add(max);
							}
						}
					} else if (informationRecue.iemeElement(i) == false) { // si l'on reçoit un bit 0
						if (informationRecue.iemeElement(bitPrec) == false) { // et que le précédent était 0 aussi
							for (int j = 0; j < (dist); j++) { // on reste à min 1/3
								informationAnalogique.add(min);
							}
							for (int j = 0; j < dist; j++) { // 2/3
								informationAnalogique.add(min);
							}
							for (int j = 0; j < dist; j++) { // 3/3
								informationAnalogique.add(min);
							}
						} else if (informationRecue.iemeElement(bitPrec) == true) { // si le précédent était un 1
							float init = max;
							for (int j = 0; j < dist; j++) { // on redescend jusq'à 0
								informationAnalogique.add(init);
								init -= pasP;
							}
							// init=0; // on se place bien à 0
							for (int j = 0; j < dist; j++) { // on descend jusqu'à min 1/3 tBit
								init += pasN;
								informationAnalogique.add(init);
							}
							for (int j = 0; j < dist; j++) {
								informationAnalogique.add(init); // on reste ensuite à min pendant 1/3 tBit
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Cette méthode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param nEch - nombre d'échantillons du signal
	 * @return - un float étant la valeur du pas
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
	 * Cette méthode permet de calculer pour true la valeur du pas
	 * 
	 * @param max  - valeur du maximum qu'atteindra la valeur float pour true
	 * @param nEch - nombre d'échantillons du signal
	 * @return - un float étant la valeur du pas
	 */
	public float calculPasNegatif(int nEch, float min) {

		float xA = 0;
		float xB = (float) nEch / 3;
		float yA = 0;
		float yB = min;

		float pas = (yB - yA) / (xB - xA);

		return pas;
	}

}