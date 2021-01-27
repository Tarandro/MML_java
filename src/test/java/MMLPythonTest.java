import org.junit.Test;

public class MMLPythonTest {
	
	
	@Test
	public void testPython1() throws Exception {
		
		MLExecutor ex = new PythonMLExecutor();
		ex.generateCode("iris.csv", "variety");
		ex.run();	
		// TODO: check assertions over return value (eg it is indeed a float value)
		
	}
	
	@Test
	public void testPython2() throws Exception {
		
		MLExecutor ex = new PythonMLExecutor();
		ex.generateCode("iris.csv", "varietyyy");
		ex.run();	
		// TODO: should raise an exception
		
	}
	
	

}
