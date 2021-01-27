import org.junit.Test;

public class MMLPythonTest {
	
	
	@Test
	public void testPython1() throws Exception {
		
		MLExecutor ex = new PythonMLExecutor();
		ex.generateCode("iris.csv", "variety");
		ex.run();
		
		
	}

}
