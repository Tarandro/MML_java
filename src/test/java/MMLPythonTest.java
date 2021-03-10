import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Test;

public class MMLPythonTest {
	
	
	@Test
	public void testPython1() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/LanguageTest/mml_LanguageTest1.json")); // no error
		ReadJsonParameters parameters = new ReadJsonParameters(string_json);
		parameters.read();
		String filename = parameters.getFilemame();
		String target_variable = parameters.getTargetVariable();
		Set<Float> set_train_sizes = parameters.getTrainSizes();
		Set<String> set_metrics = parameters.getMetrics();
		Set<Integer> set_max_depth_values = parameters.getMaxDepthValues();
		int repetition = parameters.getRepetition();
		
		// set parameters to ConfigurationML
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath(filename);
		configuration.setTarget(target_variable);
		configuration.setMetrics(set_metrics);
		
		for (float train_size : set_train_sizes) {
			// Split dataset in train/test with traintestsplit script python
			MLExecutor split = null;
			configuration.setTrainSize(train_size);
			split = new TrainTestSplit(configuration);
			split.generateCode();
			split.run();
			
			for (int max_depth : set_max_depth_values) {
				configuration.setMaxDepth(max_depth);
				
				for(int i=0; i<repetition; i++){
									
					MLExecutor ex = new PythonMLExecutor(configuration);	
					ex.generateCode();
					MLResult result = ex.run();
		
					try {
						assertTrue(result.getStringResult().contains("accuracy"));
					}
					catch (AssertionError e) {
						fail("not the good scoring");
					}
				
				}
			}
		}
	}
	
	@Test
	public void testPython2() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/LanguageTest/mml_LanguageTest2.json")); // colonne nom non valide
		ReadJsonParameters parameters = new ReadJsonParameters(string_json);
		parameters.read();
		String filename = parameters.getFilemame();
		String target_variable = parameters.getTargetVariable();
		Set<Float> set_train_sizes = parameters.getTrainSizes();
		Set<String> set_metrics = parameters.getMetrics();
		Set<Integer> set_max_depth_values = parameters.getMaxDepthValues();
		int repetition = parameters.getRepetition();
		
		// set parameters to ConfigurationML
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath(filename);
		configuration.setTarget(target_variable);
		configuration.setMetrics(set_metrics);
		
		for (float train_size : set_train_sizes) {
			// Split dataset in train/test with traintestsplit script python
			MLExecutor split = null;
			configuration.setTrainSize(train_size);
			split = new TrainTestSplit(configuration);
			split.generateCode();
			split.run();
			
			for (int max_depth : set_max_depth_values) {
				configuration.setMaxDepth(max_depth);
				
				for(int i=0; i<repetition; i++){
									
					MLExecutor ex = new PythonMLExecutor(configuration);	
					ex.generateCode();
					MLResult result = ex.run();
		
					if (result.getStringResult().contains("not found in axis")) {
						assertTrue(true);
					}
					else {
						fail("Il n'y a pas d'erreur renvoyée par Python");
					}
				
				}
			}
		}
	}
	
	@Test
	public void testPython3() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/LanguageTest/mml_LanguageTest3.json")); // no error & test autre dataset
		ReadJsonParameters parameters = new ReadJsonParameters(string_json);
		parameters.read();
		String filename = parameters.getFilemame();
		String target_variable = parameters.getTargetVariable();
		Set<Float> set_train_sizes = parameters.getTrainSizes();
		Set<String> set_metrics = parameters.getMetrics();
		Set<Integer> set_max_depth_values = parameters.getMaxDepthValues();
		int repetition = parameters.getRepetition();
		
		// set parameters to ConfigurationML
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath(filename);
		configuration.setTarget(target_variable);
		configuration.setMetrics(set_metrics);
		
		for (float train_size : set_train_sizes) {
			// Split dataset in train/test with traintestsplit script python
			MLExecutor split = null;
			configuration.setTrainSize(train_size);
			split = new TrainTestSplit(configuration);
			split.generateCode();
			split.run();
			
			for (int max_depth : set_max_depth_values) {
				configuration.setMaxDepth(max_depth);
				
				for(int i=0; i<repetition; i++){
									
					MLExecutor ex = new PythonMLExecutor(configuration);	
					ex.generateCode();
					MLResult result = ex.run();
		
					try {
						assertTrue(result.getStringResult().contains("accuracy"));
					}
					catch (AssertionError e) {
						fail("not the good scoring");
					}
				
				}
			}
		}
		
	}	
	

}
