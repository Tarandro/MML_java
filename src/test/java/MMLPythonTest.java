import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class MMLPythonTest {
	
	
	@Test
	public void testPython1() throws Exception {
		
		ConfigurationML conf = new ConfigurationML("iris.csv", "variety");
		MLExecutor ex = new PythonMLExecutor(conf);
		ex.generateCode();
		MLResult result = ex.run();	// instead of "void" we get an instance of MLResult
		// TODO: check assertions over return value (eg it is indeed a float value)
		
		
		try {
			Float.parseFloat(result.getStringResult());
		}
		catch (Exception e) {
			fail("not a float (accuracy)!");
		}
		
	}
	
	@Test
	public void testPython2() throws Exception {
		ConfigurationML conf = new ConfigurationML("iris.csv", "varietyyy");
		MLExecutor ex = new PythonMLExecutor(conf);
		ex.generateCode();
		MLResult result = ex.run();	
		// TODO: should raise an exception
		
		System.out.println(result.getStringResult());
		
		try {
			Float.parseFloat(result.getStringResult());
			fail("issue here!");
		}
		catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testR1() throws Exception {
		ConfigurationML conf = new ConfigurationML("iris.csv", "variety");
		MLExecutor ex = new RLanguageMLExecutor(conf);
		ex.generateCode();
		MLResult result = ex.run();	
	
		System.out.println(result.getStringResult());
		
		try {
			Float.parseFloat(result.getStringResult());			
		}
		catch (Exception e) {
			fail("issue here!");
		}
		
	}
	
	

}
