import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class MMLPythonTest {
	
	
	@Test
	public void testPython1() throws Exception {
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath("iris.csv");
		configuration.setTarget("variety");
		configuration.setTrainSize((float)0.5);
		Set<String> set_metrics = new HashSet<String>();
		set_metrics.add("accuracy");
		configuration.setMetrics(set_metrics);
		configuration.setMaxDepth(5);
		MLExecutor ex = new PythonMLExecutor(configuration);
		ex.generateCode();
		MLResult result = ex.run();	// instead of "void" we get an instance of MLResult
		// TODO: check assertions over return value (eg it is indeed a float value)
		
		try {
			assertTrue(result.getStringResult().contains("accuracy"));
		}
		catch (AssertionError e) {
			fail("not the good scoring");
		}
		
	}
	
	@Test
	public void testPython2() throws Exception {
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath("iris.csv");
		configuration.setTarget("varietyyy"); //non existing variable
		configuration.setTrainSize((float)0.7);
		Set<String> set_metrics = new HashSet<String>();
		set_metrics.add("accuracy");
		configuration.setMetrics(set_metrics);
		configuration.setMaxDepth(5);
		MLExecutor ex = new PythonMLExecutor(configuration);
		ex.generateCode();
		MLResult result = ex.run();
		
		if (result.getStringResult().contains("Traceback (most recent call last)")) {
			assertTrue(true);
		}
		else {
			fail("Il n'y a pas d'erreur renvoyée par Python");
		}
		
	}
	
	@Test
	public void testPython3() throws Exception {
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath("churn_dataset.csv"); //new dataset
		configuration.setTarget("Exited");
		configuration.setTrainSize((float)0.7);
		Set<String> set_metrics = new HashSet<String>();
		set_metrics.add("accuracy");
		configuration.setMetrics(set_metrics);
		configuration.setMaxDepth(5);
		MLExecutor ex = new PythonMLExecutor(configuration);
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
