package transmetteurs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({EmetteurAnalogiqueTest.class, GenerateurBruitGaussienTest.class})

public class AllTests{}