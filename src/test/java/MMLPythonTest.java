import org.junit.Test;

public class MMLPythonTest {
	
	
	@Test
	public void testPython1() throws Exception {
		
		ConfigurationML conf = new ConfigurationML("iris.csv", "variety");
		MLExecutor ex = new PythonMLExecutor(conf);
		ex.generateCode();
		ex.run();	
		// TODO: check assertions over return value (eg it is indeed a float value)
		
	}
	
	@Test
	public void testPython2() throws Exception {
		ConfigurationML conf = new ConfigurationML("iris.csv", "varietyyy");
		MLExecutor ex = new PythonMLExecutor(conf);
		ex.generateCode();
		ex.run();	
		// TODO: should raise an exception
		
	}
	
	

}
