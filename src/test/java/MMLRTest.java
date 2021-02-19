import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class MMLRTest {
	
	@Test
	public void testR1() throws Exception {
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath("iris.csv");
		configuration.setTarget("variety");
		configuration.setTrainSize((float)0.7);
		Set<String> set_metrics = new HashSet<String>();
		set_metrics.add("accuracy");
		configuration.setMetrics(set_metrics);
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
