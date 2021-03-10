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
		
		// variation points
		// dataset : not only iris.csv
		// training / testing split: not only 0.7 / 0.3
		// predictive variables/target: all except "variety"
		// ML metric: not only accuracy 
		
		// System.err.println(args[0]);
		// System.err.println(args[1]);
		
		// TargetLanguage tl = TargetLanguage.PYTHON; // 
		
		
		String str;
		
		if(args.length == 0)
		   {
			str = Files.readString(Paths.get("mml.json"));
		} else {
			str = Files.readString(Paths.get(args[0]));
					}
		
		
		
		
		// String str = "{ \"file_path\": \"iris.csv\", \"target\": 'variety' }";
		
		/*
		String str = "{\n"
				+ " \"dataset\": {\n"
				+ "   \"filename\" : \"iris.csv\",\n"
				+ "   \"separator\" : \",\"\n"
				+ " },\n"
				+ " \"training\": 70,\n"
				+ " \"testing\": 30, \n"
				+ " \"target_variable\": \"species\",\n"
				+ " \"metrics\" : [\"accuracy\", \"precision\"]\n"
				+ "  \n"
				+ "}";
				*/
		
		JSONObject obj = new JSONObject(str);
		
		//on teste le json avant de lancer le code
		JsonTest test = new JsonTest(obj);
		test.fileName();
		test.target();
		test.trainSize();
		test.language();
		
		JSONObject d = (JSONObject) obj.get("dataset");
		
		String f = "";
		try {
			f = d.getString("filename");
			}
		catch (NullPointerException errFaileName) {
			System.out.println("Donner un chemin de fichier");
			}
		// String f = obj.getString("file_path");
		String t = "";
		try {
			t = obj.getString("target_variable");
		}
		catch (NullPointerException errTarget) {
			System.out.println("Donner un nom de variable cible");
		}
		
		float train_size = (float)0.7;
		try {
			train_size = obj.getFloat("training");
		}
		catch (NullPointerException errTrainSize) {
			System.out.println("Donner une taille d'ensemble d'entraînement");
		}
		if (train_size > 1 || train_size<0) {
			throw new IllegalArgumentException("Choisir une taille d'entraînement entre 0 et 1");
		}
		
		JSONArray metrics = obj.getJSONArray("metrics");
		//String score = metrics.getString(1);
		
		// add one by one name_metric from metrics to list_metrics
		Set<String> set_metrics = new HashSet<String>();
		for(int i=0; i<metrics.length(); i++)
		{
			set_metrics.add(metrics.getString(i));
		}
		
		//JSONArray max_depth = obj.getJSONArray("max_depth");
		int max_depth_value = obj.getInt("max_depth");
		
		
		String tl = "";
		try {
			tl = obj.getString("language").toLowerCase();
		}
		catch (NullPointerException errLanguage) {
			System.out.println("Donner un nom de langage");
		}
		if (!(tl.matches("python") || tl.matches("r") || tl.matches("julia"))) {
			throw new IllegalArgumentException("Choisir Python, R ou Julia");
		}
		
		
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath(f);
		configuration.setTarget(t);
		configuration.setTrainSize(train_size);
		configuration.setMetrics(set_metrics);
		configuration.setMaxDepth(max_depth_value);
		
		
		// TODO: instead of command line arguments, we will use JSON files to configure the compilers
		// YAML, JSON, XML, etc.
		//ConfigurationML configuration = new ConfigurationML(args[0], args[1]);
		
		MLExecutor split = null;
		split = new TrainTestSplit(configuration);
		split.generateCode();
		split.run();
		
		long startTime = System.nanoTime();
		
		MLExecutor ex = null;
		
		
		if (tl.matches("python")) {			
			ex = new PythonMLExecutor(configuration);				
		}		
		else if (tl.matches("r")) {			
			ex = new RLanguageMLExecutor(configuration);			
		}
		
		else if (tl.matches("julia")) {
			ex = new JuliaMLExecutor(configuration);
			// System.err.println("Unsupported target language (TODO)");
			// TODO
			
		}
		else {
			System.err.println("Unrecognized target language");
			// TODO 
		}
		
		
		ex.generateCode();
		MLResult result = ex.run();
		
		long endTime = System.nanoTime();
		long durationInNano = (endTime - startTime);  //Total execution time in nano seconds
		long durationInMillis = TimeUnit.NANOSECONDS.toMillis(durationInNano);  //Total execution time in ms
		
		System.out.println(result.getStringResult());
		System.out.println("Execution time: "+ durationInMillis + " ms");
		
		String name_dataset = f.replace(".","").replace("/","").replace("dataset","").replace("csv", "");
		
		// add some meta information in json_result
		JSONObject json_result = result.getJSONResult();
		json_result.put("dataset", name_dataset);
		json_result.put("target", t);
		json_result.put("training", train_size);
		json_result.put("max_depth", max_depth_value);
		json_result.put("variant", tl);
		json_result.put("time_ms", durationInMillis);
		
		System.out.print(json_result);
		
		String path_log_results = "./results/log_results_"+name_dataset+".json";
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
				
		//ex.run();	
		


	}

}
