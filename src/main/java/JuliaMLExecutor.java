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
	
	private final String JULIA_OUTPUT = "./script/julia.jl";
	
	public JuliaMLExecutor(ConfigurationML configuration) {
		this.configuration = configuration;
	}

	public void generateCode() throws IOException {
		
		String file_path = configuration.getFilePath();
		String target = configuration.getTarget();
		float train_size = configuration.getTrainSize();
		Set<String> metrics = configuration.getMetrics();
		int max_depth = configuration.getMaxDepth();
		
				
		// Julia code 
		String juliaCode = "using DataFrames\r\n"
				+ "using DecisionTree\r\n"
				+ "using CSV\r\n"
				+ "using StatsBase\r\n"
				+ "using ScikitLearn\r\n"
				+ "using DecisionTree\r\n"
				+ "using Random\r\n"
				+ "using EvalMetrics\r\n"
				+ "using MLLabelUtils"
				+ "\r\n"
				+ "file_path = \""+file_path+"\"\r\n"
				+ "df_train = DataFrame(CSV.File(replace(file_path, \".csv\" => \"_train.csv\")))\r\n"
				+ "df_test = DataFrame(CSV.File(replace(file_path, \".csv\" => \"_test.csv\")))\r\n"
				+ "\r\n"
				+ "col = names(df_train)\r\n"
				+ "X_names = setdiff(col, [\"" + target + "\"])\r\n"
				+ "\r\n"
				+ "X_train = df_train[:, X_names]\r\n"
				+ "Y_train = df_train[:, :\"" + target + "\"]\r\n"
				+ "X_test = df_test[:, X_names]\r\n"
				+ "Y_test = df_test[:, :\"" + target + "\"]\r\n"
				+ "Y_enc = labelenc(df_train[:, :\"" + target + "\"])\r\n"
				+ "y_train = Int64[]\r\n"
				+ "for i in 1:length(Y_train)\r\n"
				+ "    append!(y_train, label2ind(Y_train[i], Y_enc))\r\n"
				+ "end\r\n"
				+ "y_test = Int64[]\r\n"
				+ "for i in 1:length(Y_test)\r\n"
				+ "    append!(y_test, label2ind(Y_test[i], Y_enc))\r\n"
				+ "end\n"
				+ "X_train = Matrix(X_train)\r\n"
				+ "y_train = Array(y_train)\r\n"
				+ "X_test = Matrix(X_test)\r\n"
				+ "y_test = Array(y_test)\r\n"
				+ "\r\n"
				+ "# train depth-truncated classifier\r\n"
				+ "max_depth = " + max_depth + "\n"
				+ "model = DecisionTreeClassifier(max_depth=max_depth, min_samples_split = 2)\r\n"
				+ "using ScikitLearn: fit!\r\n"
				+ "fit!(model, X_train, y_train)\r\n"
				+ "\r\n"
				+ "using ScikitLearn: predict\r\n"
				+ "pred = predict(model, X_test)\r\n"
				+ "\r\n"
				+ "using EvalMetrics:ConfusionMatrix\r\n"
				+ "using EvalMetrics:accuracy\r\n"
				+ "using EvalMetrics:precision\r\n"
				+ "using EvalMetrics:recall\r\n"
				+ "using EvalMetrics:f1_score\r\n"
				+ "\r\n"
				+ "tp_list = Float64[]\r\n"
				+ "recall_list = Float64[]\r\n"
				+ "precision_list = Float64[]\r\n"
				+ "f1score_list = Float64[]\r\n"
				+ "for i in unique(y_test)\r\n"
				+ "    predi = ifelse.(pred .== i, 1, 0)\r\n"
				+ "    y_testi = ifelse.(y_test .== i, 1, 0)\r\n"
				+ "    cmi = ConfusionMatrix(y_testi, predi)\r\n"
				+ "    append!(tp_list, true_positive(cmi))\r\n"
				+ "    append!(recall_list, recall(cmi))\r\n"
				+ "    append!(precision_list, precision(cmi))\r\n"
				+ "    append!(f1score_list, f1_score(cmi))\r\n"
				+ "end\r\n"
				+ "\r\n"
				+ "mean_accuracy = sum(tp_list)/ length(y_test)  \r\n"
				+ "print(\"accuracy : \"*string(mean_accuracy)*\"\n\")\r\n"
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
		float train_size = configuration.getTrainSize();
		int max_depth = configuration.getMaxDepth();
		
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
		
		System.out.println("Julia (Train size = "+ String.valueOf(train_size) +", Max depth = "+ String.valueOf(max_depth) +"):");

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
		if(listStrings.size() > 0) {
			result += listStrings.getLast();
		}
	
		String err; 
		while ((err = stdError.readLine()) != null) {
			result += err;
			// System.out.println(err);
		}
		
		return new MLResult(result, json_result);

	}



}
