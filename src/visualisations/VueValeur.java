package visualisations;
/** 
 * @author B. Prou
 * Updated by E. Cousin - 2021
 *
 */


import javax.swing.*;

public class VueValeur  extends Vue {

    private static final long serialVersionUID = 1917L;
    
    private JLabel jLabel;
		
		
  
    public  VueValeur (Object valeur, String nom) {   
       
	super(nom); 
	String s = " " + valeur;
	jLabel = new JLabel(s);
      	
	int xPosition = Vue.getXPosition();
	int yPosition = Vue.getYPosition();
	setLocation(xPosition, yPosition);
      	
      	add(jLabel);
	setDefaultCloseOperation(EXIT_ON_CLOSE);  
	setSize(300, 100);
	setVisible(true);  
	repaint();
    }
   
   
   
    //     /**
    //     */
    //        public void paint() {
    //          paint(getGraphics());
    //       }
    //    
    //    	
    //        public void paint(Graphics g) {
    //          if (g == null) {
    //             return;
    //          }
    //       	// effacement total
    //          g.setColor(Color.white);
    //          g.fillRect(0, 0, getWidth(), getHeight());
    //          g.setColor(Color.black);
    //       	
    //       	
    //          int x0Axe = 10;
    //          float deltaX = getContentPane().getWidth() - (2 * x0Axe);
    //       	
    //          int y0Axe = 10;
    //          float deltaY = getContentPane().getHeight() - (2 * y0Axe);      	 
    //       	
    //       	
    //          if ((yMax > 0) && (yMin <= 0)) {
    //             y0Axe += (int) (deltaY * (yMax / (yMax - yMin)));
    //          }
    //          else if ((yMax > 0) && (yMin > 0)) {
    //             y0Axe += deltaY;
    //          }
    //          else if (yMax <= 0) {
    //             y0Axe += 0;
    //          }
    //          getContentPane().getGraphics().drawLine(x0Axe, y0Axe, x0Axe + (int) deltaX + x0Axe, y0Axe);
    //          getContentPane().getGraphics().drawLine(x0Axe + (int) deltaX + x0Axe - 5, y0Axe - 5, x0Axe + (int) deltaX + x0Axe, y0Axe);
    //          getContentPane().getGraphics().drawLine(x0Axe + (int) deltaX + x0Axe - 5, y0Axe + 5, x0Axe + (int) deltaX + x0Axe, y0Axe);
    //       
    //          getContentPane().getGraphics().drawLine(x0Axe, y0Axe, x0Axe, y0Axe - (int) deltaY - y0Axe);
    //          getContentPane().getGraphics().drawLine(x0Axe + 5, 5, x0Axe, 0);
    //          getContentPane().getGraphics().drawLine(x0Axe - 5, 5, x0Axe, 0);
    //       
    //       	
    //       	// tracer la courbe
    //       
    //          float dx =  deltaX / (float) coordonnees[coordonnees.length - 1].getX();
    //          float dy = 0.0f;
    //          if ((yMax >= 0) && (yMin <= 0)) {
    //             dy =  deltaY / (yMax-yMin);
    //          }
    //          else if (yMin > 0) {
    //             dy =  deltaY / yMax;
    //          }
    //          else if (yMax < 0) { 
    //             dy =  - (deltaY / yMin);
    //          }
    //       
    //       
    //          for (int i = 1; i < coordonnees.length; i++) {
    //             int x1 = (int) (coordonnees[i-1].getX() * dx);
    //             int x2 = (int) (coordonnees[i].getX() * dx);
    //             int y1 = (int) (coordonnees[i-1].getY() * dy);
    //             int y2 = (int) (coordonnees[i].getY() * dy);
    //             getContentPane().getGraphics().drawLine( x0Axe + x1, y0Axe - y1, x0Axe + x2, y0Axe - y2);				
    //          }
    //       
    //       }
      
   
   
}
