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
		
		TargetLanguage tl = TargetLanguage.PYTHON; // TargetLanguage.PYTHON; // 
		
		
		//String str = Files.readString(Paths.get("mml.json"));
		
		/*
		String str = "{ \"file_path\": \"iris.csv\", \"target\": 'variety' }";
		JSONObject obj = new JSONObject(str);
		String f = obj.getString("file_path");
		String t = obj.getString("target");
		ConfigurationML configuration = new ConfigurationML(f, t);
		*/
		
		
		// TODO: instead of command line arguments, we will use JSON files to configure the compilers
		ConfigurationML configuration = new ConfigurationML(args[0], args[1]);
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
