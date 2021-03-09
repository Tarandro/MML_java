import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONObject;

import com.google.common.io.Files;

public class RLanguageMLExecutor extends MLExecutor {

	private static final String R_OUTPUT = "foofile.R";
		
	public RLanguageMLExecutor(ConfigurationML configuration) {
		this.configuration = configuration;
	}

	public void generateCode() throws IOException {
		
		String file_path = configuration.getFilePath();
		String target = configuration.getTarget();
		float train_size = configuration.getTrainSize();
		Set<String> metrics = configuration.getMetrics();
		int max_depth = configuration.getMaxDepth();
		
		// R code 
		String Rcode = "library(rpart)\n"
				+ "\n"
				+ "dataset = read.csv('"+ file_path +"')\n"
				+ "dataset[,'"+target+"'] = as.factor(dataset[,'"+target+"'])\n"
				+ "\n"
				+ "# Spliting dataset into training set and test set\n"
				+ "train_ind = sample(1:nrow(dataset), size = nrow(dataset)*" + train_size + ")\n"
				+ "\n"
				+ "train = dataset[train_ind, ]\n"
				+ "X_test = dataset[-train_ind, -which(colnames(dataset) ==\""+target+"\")]\n"
				+ "y_test = as.factor(dataset[-train_ind, which(colnames(dataset) ==\""+target+"\")])\n"
				+ "\n"
				+ "model = rpart(formula = "+target+"~., data = train, control = rpart.control(maxdepth =" + max_depth + "))\n"
				+ "\n"
				+ "pred = pred = as.vector(predict(model, X_test, type = 'class'))\n"
				+ "\n"
				+ "metrics = c('"+String.join("','", metrics)+"')\n"
				+ "\n"
				+ "cm = as.matrix(table(Actual = y_test, Predicted = pred))\n"
				+ "if ('confusion' %in% metrics) {print('confusion matrix :')\n"
				+ "  print(cm)}\n"
				+ "n = sum(cm)\n"
				+ "nc = nrow(cm)\n"
				+ "diag = diag(cm)\n"
				+ "rowsums = apply(cm, 1, sum)\n"
				+ "colsums = apply(cm, 2, sum)\n"
				+ "\n"
				+ "acc = sum(diag) / n \n"
				+ "precision = diag / colsums \n"
				+ "recall = diag / rowsums \n"
				+ "f1 = 2 * precision * recall / (precision + recall) \n"
				+ "macroPrecision = mean(precision) \n"
				+ "macroRecall = mean(recall) \n"
				+ "macroF1 = mean(f1) \n"
				+ "\n"
				+ "if ('accuracy' %in% metrics) {print(paste('accuracy :',acc))} \n"
				+ "if ('macro_precision' %in% metrics) {print(paste('macro precision :',macroPrecision))} \n"
				+ "if ('macro_recall' %in% metrics) {print(paste('macro recall :',macroRecall))} \n"
				+ "if ('macro_f1' %in% metrics) {print(paste('macro f1 :',macroF1))} \n"
				+ "if ('precision' %in% metrics) {print(data.frame(precision))} \n"
				+ "if ('recall' %in% metrics) {print(data.frame(recall))} \n"
				+ "if ('f1' %in% metrics) {print(data.frame(f1))} \n"
				+ "print(paste('Metrics : accuracy:',acc, '/ macro_precision:',macroPrecision, '/ macro_recall:',macroRecall, '/ macro_f1:',macroF1))\n"
				+ "";
		
		// serialize code into Python filename
		
		Files.write(Rcode.getBytes(), new File(R_OUTPUT));



	}

	public MLResult run() throws IOException {
		// execute the generated Python code
		// roughly: exec "python foofile.py"
		Process p = Runtime.getRuntime().exec("R --slave -f " + R_OUTPUT);
	
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
			listStrings.add(o.replace("\"", "").replace("[1] ", ""));
			if (!o.contains("Metrics")) {
				System.out.println(o.replace("\"", "").replace("[1] ", ""));
			} else {
				String r = o.replace("\"", "").replace(" ", "").replace("[1]", "").replace("Metrics:", "");
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
