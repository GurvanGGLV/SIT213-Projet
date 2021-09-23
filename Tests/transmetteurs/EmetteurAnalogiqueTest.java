package transmetteurs;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import static org.hamcrest.CoreMatchers.is;

public class EmetteurAnalogiqueTest 
{
	private EmetteurAnalogique emetteur1;
	private EmetteurAnalogique emetteur2;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	
	@BeforeClass
	public static void setUpClass() throws Exception{}
	
	@AfterClass
	public static void tearDownClass() throws Exception{}
	
	@Before
	public void setUp()
	{
		emetteur1 = new EmetteurAnalogique("NRZT", 50, -5, 5);
		emetteur2 = new EmetteurAnalogique("NRZT", 50, -2, 5);
	}
	
	@After
	public void tearDown(){}
	
	/**
	 * Test of calculPasPositif method, of class EmetteurAnalogique
	 */
	@Test
	public void testCalculPasPositif()
	{
		collector.checkThat("Test de calculPasPositif 1", emetteur1.calculPasPositif(emetteur1.getnEch(), emetteur1.getMax()), is(0.3F));
	}
	
	/**
	 * Test of calculPasNegatif method, of class EmetteurAnalogique
	 */
	@Test
	public void testCalculPasNegatif()
	{
		collector.checkThat("Test de calculPasNegatif 1", emetteur1.calculPasNegatif(emetteur1.getnEch(), emetteur1.getMin()), is(-0.3F));
		collector.checkThat("Test de calculPasNegatif 2", emetteur1.calculPasNegatif(emetteur1.getnEch(), emetteur2.getMin()), is(-0.12F));
	}
}
