import java.nio.file.Files;
import java.nio.file.Paths;

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
		
		TargetLanguage tl = TargetLanguage.PYTHON; // 
		
		
		String str = Files.readString(Paths.get("mml.json"));
		
		
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
		int training_size = obj.getInt("training"); // TODO
		ConfigurationML configuration = new ConfigurationML(f, t);
		
		
		
		// TODO: instead of command line arguments, we will use JSON files to configure the compilers
		// YAML, JSON, XML, etc.
		//ConfigurationML configuration = new ConfigurationML(args[0], args[1]);
		MLExecutor ex = null;
		
		
		if (tl == TargetLanguage.PYTHON) {			
			ex = new PythonMLExecutor(configuration);				
		}		
		else if (tl == TargetLanguage.R) {			
			ex = new RLanguageMLExecutor(configuration);			
		}
		
		else if (tl == TargetLanguage.JULIA) {
			// ex = new JuliaMLExecutor();
			System.err.println("Unsupported target language (TODO)");
			// TODO
			
		}
		else {
			System.err.println("Unrecognized target language");
			// TODO 
		}
		
		
		ex.generateCode();
		ex.run();	
		
		
		
		

		
		

	}

}
