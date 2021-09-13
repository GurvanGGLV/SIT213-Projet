package visualisations;
/** 
 * @author B. Prou
 * Updated by E. Cousin - 2021
 *
 */

import java.awt.*;
import java.awt.geom.*;

public class VueCourbe  extends Vue {
    
    private static final long serialVersionUID = 1917L;
   
    private Point2D.Float [] coordonnees;
    private float yMax = 0;
    private float yMin = 0;
   	
   	
    public  VueCourbe (boolean [] valeurs, int nbPixels, String nom) {   
       
	super(nom); 
      	
	int xPosition = Vue.getXPosition();
	int yPosition = Vue.getYPosition();
	setLocation(xPosition, yPosition);
      	
	this.coordonnees = new Point2D.Float [(2 * valeurs.length) + 1];
	yMax = 1;
	yMin = 0;
      	
	coordonnees[0] = new Point2D.Float(0, 0);
      	
	for (int i = 0, j = 0; i < valeurs.length; i++, j+= 2) {
            if (valeurs[i]) {
		coordonnees[j+1] = new Point2D.Float(i, 1);
		coordonnees[j+2] = new Point2D.Float(i+1, 1);
            }
            else {
		coordonnees[j+1] = new Point2D.Float(i, 0);
		coordonnees[j+2] = new Point2D.Float(i+1, 0);
            }
	}
      
	setDefaultCloseOperation(EXIT_ON_CLOSE);  
	int largeur = (valeurs.length * nbPixels) + 10;
	if (largeur > 1000)
            largeur = 1000;
	setSize(largeur, 200);
	setVisible(true);  
	repaint();
    }
   
   
    public  VueCourbe (float [] valeurs, String nom) {   
       
	super(nom);      
      
	int xPosition = Vue.getXPosition();
	int yPosition = Vue.getYPosition();
	setLocation(xPosition, yPosition);
      
	this.coordonnees = new Point2D.Float [valeurs.length];
	yMax = 0;
	yMin = 0;
      	
	for (int i = 0; i < valeurs.length; i++) {
            if (valeurs[i] > yMax)
		yMax = valeurs[i];
            if (valeurs[i] < yMin)
		yMin = valeurs[i];
            coordonnees[i] = new Point2D.Float(i, valeurs[i]);
	}
      
	setDefaultCloseOperation(EXIT_ON_CLOSE);  
	int largeur = valeurs.length  + 10;
	if (largeur > 1000)
            largeur = 1000;
	setSize(largeur, 200);
	setVisible(true);  
	repaint();
    }
   
   
    public  void changer (boolean [] valeurs) {   
             	
	this.coordonnees = new Point2D.Float [(2 * valeurs.length) + 1];
	yMax = 1;
	yMin = 0;
      	
	coordonnees[0] = new Point2D.Float(0, 0);
      	
	for (int i = 0, j = 0; i < valeurs.length; i++, j+= 2) {
            if (valeurs[i]) {
		coordonnees[j+1] = new Point2D.Float(i, 1);
		coordonnees[j+2] = new Point2D.Float(i+1, 1);
            }
            else {
		coordonnees[j+1] = new Point2D.Float(i, 0);
		coordonnees[j+2] = new Point2D.Float(i+1, 0);
            }
	}
      
	paint();
    }
   
    public  void changer (float [] valeurs) {   
             
	this.coordonnees = new Point2D.Float [valeurs.length];
	yMax = 0;
	yMin = 0;
      	
	for (int i = 0; i < valeurs.length; i++) {
            if (valeurs[i] > yMax)
		yMax = valeurs[i];
            if (valeurs[i] < yMin)
		yMin = valeurs[i];
            coordonnees[i] = new Point2D.Float(i, valeurs[i]);
	}
      
	paint();
    }
   
   
   
    /**
     */
    public void paint() {
	paint(getGraphics());
    }
   
   	
    public void paint(Graphics g) {
	if (g == null) {
            return;
	}
      	// effacement total
	g.setColor(Color.white);
	g.fillRect(0, 0, getWidth(), getHeight());
	g.setColor(Color.black);
      	
      	
	int x0Axe = 10;
	float deltaX = getContentPane().getWidth() - (2 * x0Axe);
      	
	int y0Axe = 10;
	float deltaY = getContentPane().getHeight() - (2 * y0Axe);      	 
      	
      	
	if ((yMax > 0) && (yMin <= 0)) {
            y0Axe += (int) (deltaY * (yMax / (yMax - yMin)));
	}
	else if ((yMax > 0) && (yMin > 0)) {
            y0Axe += deltaY;
	}
	else if (yMax <= 0) {
            y0Axe += 0;
	}
	getContentPane().getGraphics().drawLine(x0Axe, y0Axe, x0Axe + (int) deltaX + x0Axe, y0Axe);
	getContentPane().getGraphics().drawLine(x0Axe + (int) deltaX + x0Axe - 5, y0Axe - 5, x0Axe + (int) deltaX + x0Axe, y0Axe);
	getContentPane().getGraphics().drawLine(x0Axe + (int) deltaX + x0Axe - 5, y0Axe + 5, x0Axe + (int) deltaX + x0Axe, y0Axe);
      
	getContentPane().getGraphics().drawLine(x0Axe, y0Axe, x0Axe, y0Axe - (int) deltaY - y0Axe);
	getContentPane().getGraphics().drawLine(x0Axe + 5, 5, x0Axe, 0);
	getContentPane().getGraphics().drawLine(x0Axe - 5, 5, x0Axe, 0);
      
      	
      	// tracer la courbe
      
	float dx =  deltaX / (float) coordonnees[coordonnees.length - 1].getX();
	float dy = 0.0f;
	if ((yMax >= 0) && (yMin <= 0)) {
            dy =  deltaY / (yMax-yMin);
	}
	else if (yMin > 0) {
            dy =  deltaY / yMax;
	}
	else if (yMax < 0) { 
            dy =  - (deltaY / yMin);
	}
      
      
	for (int i = 1; i < coordonnees.length; i++) {
            int x1 = (int) (coordonnees[i-1].getX() * dx);
            int x2 = (int) (coordonnees[i].getX() * dx);
            int y1 = (int) (coordonnees[i-1].getY() * dy);
            int y2 = (int) (coordonnees[i].getY() * dy);
            getContentPane().getGraphics().drawLine( x0Axe + x1, y0Axe - y1, x0Axe + x2, y0Axe - y2);				
	}
      	
      
      
    }
      
   
   
}
