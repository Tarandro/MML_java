import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.LinkedList;

import com.google.common.io.Files;

public class JuliaMLExecutor extends MLExecutor {
	
	private final String JULIA_OUTPUT = "julia.jl";
	
	public JuliaMLExecutor(ConfigurationML configuration) {
		this.configuration = configuration;
	}

	// TODO: refactoring of the code is needed since anti-pattern/bad smell https://fr.wikipedia.org/wiki/Code_smell#Long_Parameter_List
	public void generateCode() throws IOException {
		
		String file_path = configuration.getFilePath();
		String target = configuration.getTarget();
		float train_size = configuration.getTrainSize();
		Set<String> metrics = configuration.getMetrics();
		int max_depth = configuration.getMaxDepth();
		
				
		// Python code 
		String juliaCode = "using DataFrames\n"
				+ "using DecisionTree\n"
				+ "using CSV\n"
				+ "using StatsBase\n"
				+ "using ScikitLearn\n"
				+ "using Random\n"
				+ "df = DataFrame(CSV.File(\"" + file_path + "\"))\r\n"
				+ "\r\n"
				+ "# split the dataset \r\n"
				+ "sample = randsubseq(1:size(df,1), 0.8)\r\n"
				+ "train = df[sample, :]\r\n"
				+ "notsample = [i for i in 1:size(df,1) if isempty(searchsorted(sample, i))]\r\n"
				+ "test = df[notsample, :]\r\n"
				+ "col = names(df)\r\n"
				+ "X_names = setdiff(col, [\"" + target + "\"])\r\n"
				+ "X_train = train[:, X_names]\r\n"
				+ "Y_train = train[:, :\"" + target + "\"]\r\n"
				+ "X_test = test[:, X_names]\r\n"
				+ "Y_test = test[:, :\"" + target + "\"]\r\n"
				+ "\r\n"
				+ "X_train = Matrix(X_train)\r\n"
				+ "Y_train = Array(Y_train)\r\n"
				+ "X_test = Matrix(X_test)\r\n"
				+ "Y_test = Array(Y_test)\r\n"
				+ "\r\n"
				+ "# train depth-truncated classifier\r\n"
				+ "max_depth = " + max_depth + "\n"
				+ "model = DecisionTreeClassifier(max_depth=max_depth)\r\n"
				+ "using ScikitLearn: fit!\r\n"
				+ "fit!(model, X_train, Y_train)\r\n"
				+ "# run n-fold cross validation over 3 CV folds\r\n"
				+ "# See ScikitLearn.jl for installation instructions\r\n"
				+ "using ScikitLearn.CrossValidation: cross_val_score\r\n"
				+ "accuracy = cross_val_score(model, X_test, Y_test, cv=3)\r\n"
				+ "println(mean(accuracy))";
				
		// serialize code into Julia filename
				
		Files.write(juliaCode.getBytes(), new File(JULIA_OUTPUT));

	}

	public MLResult run() throws IOException {
		String target = configuration.getTarget();
		
		// execute the generated Julia code
		// roughly: exec "julia foofile.py"
		Process p = Runtime.getRuntime().exec("C:\\Users\\Alex\\AppData\\Local\\Programs\\Julia 1.5.3\\bin\\julia " + JULIA_OUTPUT);
	
		// output
		BufferedReader stdInput = new BufferedReader(new 
				InputStreamReader(p.getInputStream()));
	
		// error
		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(p.getErrorStream()));
		
		String result = "";
	
		String o;
		LinkedList<String> listStrings = new LinkedList<String>();
		
		while ((o = stdInput.readLine()) != null) {
			listStrings.add(o);
			//System.out.println(o); //comment or not
		}
		result += listStrings;//.getLast(); //permet de ne garder que la ligne avec la métrique
	
		String err; 
		while ((err = stdError.readLine()) != null) {
			result += err;
			// System.out.println(err);
		}
		
		return new MLResult(result);

	}



}
