package transmetteurs;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;

import information.*;
import simulateur.*;
import sources.*;
import transmetteurs.EmetteurAnalogique;
import transmetteurs.GenerateurBruitGaussien;
import destinations.*;

import static org.hamcrest.CoreMatchers.is;

public class GenerateurBruitGaussienTest 
{
	private SourceFixe source = null;
	private EmetteurAnalogique emetteurAnalogique;
	private GenerateurBruitGaussien bruit1;
	private GenerateurBruitGaussien bruit2;
	private Information<Float> informationAnalogique1;
	private Information<Float> informationAnalogique2;
	
	
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	@BeforeClass
	public static void setUpClass() throws Exception{}
	
	@AfterClass
	public static void tearDownClass() throws Exception{}
	
	@Before
	public void setUp()
	{
		source = new SourceFixe("00101110100011011001110101000110");
		
		emetteurAnalogique = new EmetteurAnalogique("NRZT", 50, -5, 5);
		
		bruit1 = new GenerateurBruitGaussien(2, 30);
		bruit2 = new GenerateurBruitGaussien(3, 50);
		
		source.connecter(emetteurAnalogique);
		emetteurAnalogique.connecter(bruit1);
		emetteurAnalogique.connecter(bruit2);
		
		try {
			source.emettre();
		} catch (InformationNonConformeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown(){}
	
	/**
	 * Test of calculPuissance method, of class EmetteurAnalogique
	 */
	@Test
	public void testCalculPuissance()
	{
		informationAnalogique1 = bruit1.informationRecue;
		informationAnalogique2 = bruit2.informationRecue;
		
		//TODO calculer valeur théorique pour comparer
		collector.checkThat("Test de calculPuissance 1", bruit1.calculPuissance(informationAnalogique1), is(0.0F));
		collector.checkThat("Test de calculPuissance 2", bruit2.calculPuissance(informationAnalogique2), is(0.0F));
	}
	
	/**
	 * Test of calculBruit method, of class EmetteurAnalogique
	 */
	@Test
	public void testCalculBruit()
	{	
		//TODO calculer valeur théorique pour comparer
		collector.checkThat("Test de calculBruit 1", bruit1.calculBruit(3,null), is(0.0F)); // null pour la seed. Verifier si on peut changer ca
		collector.checkThat("Test de calculBruit 2", bruit2.calculBruit(5,null), is(0.0F));
	}
	
	@Test
	public void testCalculSigma()
	{
		//TODO calculer valeur théorique pour comparer
		collector.checkThat("Test de calculBruit 1", bruit1.calculSigma(), is(0.0F));
		collector.checkThat("Test de calculBruit 2", bruit2.calculSigma(), is(0.0F));
	}
}