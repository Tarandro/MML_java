import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;


public class MMLMain {


	public static void main(String[] args) throws Exception {
		
		String string_json;
		
		// json to string
		if(args.length == 0)
		   {
			string_json = Files.readString(Paths.get("mml.json"));
		} else {
			string_json = Files.readString(Paths.get(args[0]));
					}
		
		// get parameters :
		ReadJsonParameters parameters = new ReadJsonParameters(string_json);
		
		parameters.read();
		String filename = parameters.getFilemame();
		String target_variable = parameters.getTargetVariable();
		Set<Float> set_train_sizes = parameters.getTrainSizes();
		Set<String> set_metrics = parameters.getMetrics();
		Set<Integer> set_max_depth_values = parameters.getMaxDepthValues();
		Set<String> set_languages = parameters.getLanguages();
		
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
			
			// start of the timer:
			long startTime = System.nanoTime();
		
			for (int max_depth : set_max_depth_values) {
				configuration.setMaxDepth(max_depth);
				
				for (String language : set_languages) {
					MLExecutor ex = null;
					// Detection of the language
					if (language.matches("python")) {			
						ex = new PythonMLExecutor(configuration);				
					}		
					else if (language.matches("r")) {			
						ex = new RLanguageMLExecutor(configuration);			
					}
					
					else if (language.matches("julia")) {
						ex = new JuliaMLExecutor(configuration);
					}
					else {
						System.err.println("Unrecognized target language");
					}
					
					
					ex.generateCode();
					MLResult result = ex.run();
					
					long endTime = System.nanoTime();
					long durationInNano = (endTime - startTime);  //Total execution time in nano seconds
					long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);  //Total execution time in ms
					
					System.out.println(result.getStringResult());
					System.out.println("Execution time: "+ durationInMillis + " ms");
					
					String name_dataset = filename.replace(".","").replace("/","").replace("dataset","").replace("csv", "");
					
					// add some meta information in json_result
					JSONObject json_result = result.getJSONResult();
					json_result.put("dataset", name_dataset);
					json_result.put("target", target_variable);
					json_result.put("training", train_size);
					json_result.put("max_depth", max_depth);
					json_result.put("variant", language);
					json_result.put("time_ms", durationInMillis);
					
					// save score and meta_information in log_results :
					String path_log_results = "./results/log_results_"+name_dataset+"_train_size_"+ String.valueOf(Math.round(train_size*100)) +".json";
					Path path = Paths.get(path_log_results);
					
					JSONObject log_results = new JSONObject();
					// if file exist, add json_result to file with key : length(log_results) + 1
				    if (Files.exists(path)) {
				    	String log_results_str = Files.readString(Paths.get(path_log_results));
						log_results = new JSONObject(log_results_str);
							
						log_results.put(String.valueOf(log_results.length()+1), json_result);
					// if not, create a json file and add json_result with key = 1
				    }else{
				    	log_results = new JSONObject();
						log_results.put("1", json_result);
		
				    }
				    FileWriter file = new FileWriter(path_log_results);
				    file.write(log_results.toString());
				    file.flush();
				    file.close();
				    System.out.println("");
				}
			}
		}
		System.out.println("Execution Finish");
	}

}
