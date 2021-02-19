import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MMLPythonTest {
	
	
	@Test
	public void testPython1() throws Exception {
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath("iris.csv");
		configuration.setTarget("variety");
		configuration.setTrainSize((float)0.5);
		configuration.setScore("accuracy");
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
		configuration.setTarget("varietyyy");
		configuration.setTrainSize((float)0.7);
		configuration.setScore("accuracy");
		configuration.setMaxDepth(5);
		MLExecutor ex = new PythonMLExecutor(configuration);
		ex.generateCode();
		
		try {
			MLResult result = ex.run();
			assertTrue(result.getStringResult().contains("accuracy"));
		}
		catch (java.util.NoSuchElementException e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testPython3() throws Exception {
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath("churn_dataset.csv");
		configuration.setTarget("Exited");
		configuration.setTrainSize((float)0.7);
		configuration.setScore("accuracy");
		configuration.setMaxDepth(5);
		MLExecutor ex = new PythonMLExecutor(configuration);
		ex.generateCode();
		MLResult result = ex.run();	
		// TODO: should raise an exception
		
		System.out.println(result.getStringResult());
		
		try {
			assertTrue(result.getStringResult().contains("accuracy"));
		}
		catch (AssertionError e) {
			fail("not the good scoring");
		}
		
	}	
	

}
