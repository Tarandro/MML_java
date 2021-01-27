public class MMLMain {


	public static void main(String[] args) throws Exception {
		
		// variation points
		// dataset : not only iris.csv
		// training / testing split: not only 0.7 / 0.3
		// predictive variables/target: all except "variety"
		// ML metric: not only accuracy 
		
		// System.err.println(args[0]);
		// System.err.println(args[1]);
		
		TargetLanguage tl = TargetLanguage.PYTHON; // TargetLanguage.R
		MLExecutor ex = null;
		if (tl == TargetLanguage.PYTHON) {			
			ex = new PythonMLExecutor();				
		}		
		else if (tl == TargetLanguage.R) {			
			ex = new RLanguageMLExecutor();			
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
		
		// TODO: instead of command line arguments, we will use JSON files to configure the compilers
		ex.generateCode(args[0], args[1]);
		ex.run();	
		
		
		
		

		
		

	}

}
