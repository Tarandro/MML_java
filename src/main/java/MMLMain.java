import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;


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
		JSONObject d = (JSONObject) obj.get("dataset");
		String f = d.getString("filename");
		// String f = obj.getString("file_path"); // args[0]
		String t = obj.getString("target_variable"); // args[1]
		float train_size = obj.getFloat("training"); // TODO
		JSONArray metrics = obj.getJSONArray("metrics");
		String score = metrics.getString(1);
		JSONArray max_depth = obj.getJSONArray("max_depth");
		int max_depth_value = max_depth.getInt(2);
		String tl = obj.getString("language").toLowerCase();
		ConfigurationML configuration = new ConfigurationML();
		configuration.setFilePath(f);
		configuration.setTarget(t);
		configuration.setScore(score);
		configuration.setMaxDepth(max_depth_value);
		
		
		
		// TODO: instead of command line arguments, we will use JSON files to configure the compilers
		// YAML, JSON, XML, etc.
		//ConfigurationML configuration = new ConfigurationML(args[0], args[1]);
		MLExecutor ex = null;
		
		
		if (tl.matches("python")) {			
			ex = new PythonMLExecutor(configuration);				
		}		
		else if (tl.matches("r")) {			
			ex = new RLanguageMLExecutor(configuration);			
		}
		
		else if (tl.matches("julia")) {
			// ex = new JuliaMLExecutor();
			System.err.println("Unsupported target language (TODO)");
			// TODO
			
		}
		else {
			System.err.println("Unrecognized target language");
			// TODO 
		}
		
		
		ex.generateCode();
		MLResult result = ex.run();
		System.out.println(result.getStringResult());
				
		//ex.run();	
		
		


	}

}
