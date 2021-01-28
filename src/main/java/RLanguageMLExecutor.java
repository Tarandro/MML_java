import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
		String score = configuration.getScore();
		int max_depth = configuration.getMaxDepth();
		
		// R code 
		String Rcode = "library(rpart)\n"
				+ "library(MLmetrics, quietly = TRUE, warn.conflicts = FALSE)\n"
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
				+ "pred = predict(model, X_test, type = 'class')\n"
				+ "\n"
				+ "score = \""+score+"\" \n"
				+ "acc = sum(pred == y_test)/length(y_test)\n"
				+ "precision = Precision(y_test, pred) \n"
				+ "if (score == 'accuracy') {print(paste('accuracy :',acc))} else if (score == 'precision') {print(paste('precision :',precision))} \n"
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
		while ((o = stdInput.readLine()) != null) {
			result += o;
			// System.out.println(o);
		}
	
		String err; 
		while ((err = stdError.readLine()) != null) {
			result += err;
			// System.out.println(err);
		}
		
		return new MLResult(result);

	}

}
