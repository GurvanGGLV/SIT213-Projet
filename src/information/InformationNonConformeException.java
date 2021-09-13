package information;

public class InformationNonConformeException extends Exception {
   
    private static final long serialVersionUID = 1917L;
    
    public InformationNonConformeException() {
	super();
    }
   
    public InformationNonConformeException(String motif) {
	super(motif);
    }
}
