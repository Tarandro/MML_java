import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

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
		String juliaCode = "using DataFrames\r\n"
				+ "using DecisionTree\r\n"
				+ "using CSV\r\n"
				+ "using StatsBase\r\n"
				+ "using ScikitLearn\r\n"
				+ "using DecisionTree\r\n"
				+ "using Random\r\n"
				+ "using EvalMetrics\r\n"
				+ "\r\n"
				+ "df = DataFrame(CSV.File(\"" + file_path + "\"))\r\n"
				+ "\r\n"
				+ "rename!(df,[:sepal_length, :sepal_width,:petal_length, :petal_width, :variety])\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "# split the dataset \r\n"
				+ "sample = randsubseq(1:size(df,1), 0.8)\r\n"
				+ "train = df[sample, :]\r\n"
				+ "notsample = [i for i in 1:size(df,1) if isempty(searchsorted(sample, i))]\r\n"
				+ "test = df[notsample, :]\r\n"
				+ "\r\n"
				+ "col = names(df)\r\n"
				+ "X_names = setdiff(col, [\"" + target + "\"])\r\n"
				+ "\r\n"
				+ "X_train = train[:, X_names]\r\n"
				+ "Y_train = train[:, :\"" + target + "\"]\r\n"
				+ "X_test = test[:, X_names]\r\n"
				+ "Y_test = test[:, :\"" + target + "\"]\r\n"
				+ "println(length(Y_train))\n"
				+ "println(length(Y_test))\n"
				+ "\r\n"
				+ "y_train = Int64[]\r\n"
				+ "for i in 1:length(Y_train)\r\n"
				+ "    if Y_train[i] === \"Setosa\"\r\n"
				+ "        append!(y_train, 0)\r\n"
				+ "    end\r\n"
				+ "    if Y_train[i] === \"Versicolor\"\r\n"
				+ "        append!(y_train, 1)\r\n"
				+ "    end\r\n"
				+ "    if Y_train[i] === \"Virginica\"\r\n"
				+ "        append!(y_train, 2)\r\n"
				+ "    end\r\n"
				+ "end\r\n"
				+ "\r\n"
				+ "y_test = Int64[]\r\n"
				+ "for i in 1:length(Y_test)\r\n"
				+ "    if Y_test[i] === \"Setosa\"\r\n"
				+ "        append!(y_test, 0)\r\n"
				+ "    end\r\n"
				+ "    if Y_test[i] === \"Versicolor\"\r\n"
				+ "        append!(y_test, 1)\r\n"
				+ "    end\r\n"
				+ "    if Y_test[i] === \"Virginica\"\r\n"
				+ "        append!(y_test, 2)\r\n"
				+ "    end\r\n"
				+ "end\r\n"
				+ "\r\n"
				+ "X_train = Matrix(X_train)\r\n"
				+ "y_train = Array(y_train)\r\n"
				+ "X_test = Matrix(X_test)\r\n"
				+ "y_test = Array(y_test)\r\n"
				+ "\r\n"
				+ "# train depth-truncated classifier\r\n"
				+ "max_depth = " + max_depth + "\n"
				+ "model = DecisionTreeClassifier(max_depth=max_depth)\r\n"
				+ "using ScikitLearn: fit!\r\n"
				+ "fit!(model, X_train, y_train)\r\n"
				+ "# using ScikitLearn: predict\r\n"
				+ "# predict(model, [5.9,3.0,5.1,1.9])\r\n"
				+ "# get the probability of each label\r\n"
				+ "# predict_proba(model, [5.9,3.0,5.1,1.9])\r\n"
				+ "# println(get_classes(model)) # returns the ordering of the columns in predict_proba's output\r\n"
				+ "# run n-fold cross validation over 3 CV folds\r\n"
				+ "# See ScikitLearn.jl for installation instructions\r\n"
				+ "using ScikitLearn.CrossValidation: cross_val_score\r\n"
				+ "accuracy = cross_val_score(model, X_test, y_test, cv=3)\r\n"
				+ "mean_accuracy = mean(accuracy)\r\n"
				+ "print(\"accuracy : \"*string(mean_accuracy)*\"\n\")\r\n"
				+ "\r\n"
				+ "using ScikitLearn: predict\r\n"
				+ "pred = predict(model, X_test)\r\n"
				+ "\r\n"
				+ "using EvalMetrics:ConfusionMatrix\r\n"
				+ "using EvalMetrics:precision\r\n"
				+ "using EvalMetrics:recall\r\n"
				+ "using EvalMetrics:f1_score\r\n"
				+ "\r\n"
				+ "recall_list = Float64[]\r\n"
				+ "precision_list = Float64[]\r\n"
				+ "f1score_list = Float64[]\r\n"
				+ "for i in unique(y_test)\r\n"
				+ "    predi = ifelse.(pred .== i, 1, 0)\r\n"
				+ "    y_testi = ifelse.(y_test .== i, 1, 0)\r\n"
				+ "    cmi = ConfusionMatrix(y_testi, predi)\r\n"
				+ "    append!(recall_list, recall(cmi))\r\n"
				+ "    append!(precision_list, precision(cmi))\r\n"
				+ "    append!(f1score_list, f1_score(cmi))\r\n"
				+ "end\r\n"
				+ "\r\n"
				+ "macro_recall = mean(recall_list)\r\n"
				+ "print(\"macro_recall : \"*string(macro_recall)*\"\n\")\r\n"
				+ "\r\n"
				+ "macro_precision = mean(precision_list)\r\n"
				+ "print(\"macro_precision : \"*string(macro_precision)*\"\n\")\r\n"
				+ "\r\n"
				+ "macro_f1 = mean(f1score_list)\r\n"
				+ "print(\"macro_f1 : \"*string(macro_f1)*\"\n\")\r\n"
				+ "print(\"Metrics : accuracy: \"*string(mean_accuracy)*\" / macro_precision: \"*string(macro_precision)*\" / macro_recall: \"*string(macro_recall)*\" / macro_f1: \"*string(macro_f1))\r\n";
				
		// serialize code into Julia filename
				
		Files.write(juliaCode.getBytes(), new File(JULIA_OUTPUT));

	}

	public MLResult run() throws IOException {
		String target = configuration.getTarget();
		
		// execute the generated Julia code
		// roughly: exec "julia foofile.py"
		Process p = Runtime.getRuntime().exec("julia " + JULIA_OUTPUT);
	
		// output
		BufferedReader stdInput = new BufferedReader(new 
				InputStreamReader(p.getInputStream()));
	
		// error
		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(p.getErrorStream()));
		
		String result = "";
	
		String o;
		LinkedList<String> listStrings = new LinkedList<String>();
		
		JSONObject json_result = new JSONObject(); 

		while ((o = stdInput.readLine()) != null) {
			listStrings.add(o);
			if (!o.contains("Metrics")) {
				System.out.println(o);
			} else {
				String r = o.replace(" ", "").replace("Metrics:", "");
				String[] split_metrics = r.split("/");
				for (int i = 0; i < split_metrics.length; i++) {
					json_result.put(split_metrics[i].split(":")[0], Float.parseFloat(split_metrics[i].split(":")[1]));
				}
			}
		}
		result += listStrings.getLast();//.getLast(); //permet de ne garder que la ligne avec la métrique
	
		String err; 
		while ((err = stdError.readLine()) != null) {
			result += err;
			// System.out.println(err);
		}
		
		return new MLResult(result, json_result);

	}



}