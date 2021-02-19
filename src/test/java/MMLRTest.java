import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MMLRTest {
	
	@Test
	public void testR1() throws Exception {
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath("iris.csv");
		configuration.setTarget("variety");
		configuration.setTrainSize((float)0.7);
		configuration.setScore("accuracy");
		configuration.setMaxDepth(5);
		MLExecutor ex = new RLanguageMLExecutor(configuration);
		ex.generateCode();
		MLResult result = ex.run();	
	
		System.out.println(result.getStringResult());
		
		try {
			assertTrue(result.getStringResult().contains("accuracy"));		
		}
		catch (AssertionError e) {
			fail("not the good scoring");
		}
		
	}
	
	

}
