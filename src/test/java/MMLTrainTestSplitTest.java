import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.junit.Test;

public class MMLTrainTestSplitTest {

	@Test
	public void testTrainTestSplit1() throws Exception {
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
			
			Path path_train = Paths.get(filename.replace(".csv","_train.csv"));
			Path path_test = Paths.get(filename.replace(".csv","_test.csv"));
			
			if (Files.exists(path_train)) {
				assertTrue(true);
			}
			else {
				fail("Train dataset n'a pas été construit");
			}
			if (Files.exists(path_test)) {
				assertTrue(true);
			}
			else {
				fail("Test dataset n'a pas été construit");
			}
		}
	}
	
	
	@Test
	public void testTrainTestSplit3() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/LanguageTest/mml_LanguageTest3.json")); // no error & autre dataset
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
			
			Path path_train = Paths.get(filename.replace(".csv","_train.csv"));
			Path path_test = Paths.get(filename.replace(".csv","_test.csv"));
			
			if (Files.exists(path_train)) {
				assertTrue(true);
			}
			else {
				fail("Train dataset n'a pas été construit");
			}
			if (Files.exists(path_test)) {
				assertTrue(true);
			}
			else {
				fail("Test dataset n'a pas été construit");
			}
		}
	}
	
	@Test
	public void testTrainTestSplit4() throws Exception {
		String string_json = Files.readString(Paths.get("./mml_test/LanguageTest/mml_LanguageTest4.json")); // wrong dataset name
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
			MLResult result = split.run();
			
			if (result.getStringResult().contains("FileNotFoundError")) {
				assertTrue(true);
			}
			else {
				fail("Il n'y a pas d'erreur renvoyée par Python");
			}
		}
	}
}
