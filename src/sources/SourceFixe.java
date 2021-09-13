package sources;

import information.Information;

public class SourceFixe extends Source<Boolean> {

	public SourceFixe (String message) {
		
		super();
		
		informationGeneree = new Information<Boolean>();
		
		// on vérifie la forme du message
		for (int i=0 ; i < message.length() ; i++ ) {
			
			if ( message.charAt(i) == '0' ) {
				informationGeneree.add(false);
			}
			else {
				informationGeneree.add(true);
			}
			
		}
		//System.out.println(informationGeneree);
	}
		
}
