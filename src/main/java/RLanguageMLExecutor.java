import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONObject;

import com.google.common.io.Files;

public class RLanguageMLExecutor extends MLExecutor {

	private static final String R_OUTPUT = "./script/foofile.R";
		
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
				+ "file_path = '" + file_path + "'\n"
				+ "df_train = read.csv(gsub('.csv','_train.csv',file_path))\n"
				+ "df_train[,'"+target+"'] = as.factor(df_train[,'"+target+"'])\n"
				+ "df_test = read.csv(gsub('.csv','_test.csv',file_path))\n"
				+ "df_test[,'"+target+"'] = as.factor(df_test[,'"+target+"'])\n"
				+ "\n"
				+ "X_test = df_test[, -which(colnames(df_test) ==\""+target+"\")]\n"
				+ "y_test = as.factor(df_test[, which(colnames(df_test) ==\""+target+"\")])\n"
				+ "\n"
				+ "model = rpart(formula = "+target+"~., data = df_train, control = rpart.control(maxdepth =" + max_depth + "))\n"
				+ "\n"
				+ "pred = as.vector(predict(model, X_test, type = 'class'))\n"
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
		
		
		Files.write(Rcode.getBytes(), new File(R_OUTPUT));



	}

	public MLResult run() throws IOException {
		float train_size = configuration.getTrainSize();
		int max_depth = configuration.getMaxDepth();

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
		
		System.out.println("R (Train size = "+ String.valueOf(train_size) +", Max depth = "+ String.valueOf(max_depth) +"):");

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
