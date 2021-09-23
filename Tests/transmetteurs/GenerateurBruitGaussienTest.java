package transmetteurs;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;

import information.Information;

import static org.hamcrest.CoreMatchers.is;

public class GenerateurBruitGaussienTest 
{
	private GenerateurBruitGaussien bruit1;
	private GenerateurBruitGaussien bruit2;
	private Information<Float> informationAnalogique1 = bruit1.getInformationRecue();
	private Information<Float> informationAnalogique2 = bruit2.getInformationRecue();
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	@BeforeClass
	public static void setUpClass() throws Exception{}
	
	@AfterClass
	public static void tearDownClass() throws Exception{}
	
	@Before
	public void setUp()
	{
		bruit1 = new GenerateurBruitGaussien(2, 30);
		bruit2 = new GenerateurBruitGaussien(3, 50);
	}
	
	@After
	public void tearDown(){}
	
	/**
	 * Test of calculPuissance method, of class EmetteurAnalogique
	 */
	@Test
	public void testCalculPuissance()
	{
		collector.checkThat("Test de calculPuissance 1", bruit1.calculPuissance(informationAnalogique1), is(0.0F));
		collector.checkThat("Test de calculPuissance 2", bruit2.calculPuissance(informationAnalogique2), is(0.0F));
	}
	
	/**
	 * Test of calculBruit method, of class EmetteurAnalogique
	 */
	@Test
	public void testCalculBruit()
	{
		collector.checkThat("Test de calculBruit 1", bruit1.calculBruit(3), is(0.0F));
		collector.checkThat("Test de calculBruit 2", bruit2.calculBruit(5), is(0.0F));
	}
	
	public void testCalculSigma()
	{
		collector.checkThat("Test de calculBruit 1", bruit1.calculSigma(), is(0.0F));
		collector.checkThat("Test de calculBruit 1", bruit2.calculSigma(), is(0.0F));
	}
}
