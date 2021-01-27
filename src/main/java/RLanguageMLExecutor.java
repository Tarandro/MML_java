import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.io.Files;

public class RLanguageMLExecutor extends MLExecutor {

	private static final String R_OUTPUT = "foofile.R";

	public void generateCode(String file_path, String target) throws IOException {
		// R code 
		String Rcode = "library(rpart)\n"
				+ "\n"
				+ "dataset = read.csv('"+ file_path +"')\n"
				+ "\n"
				+ "# Spliting dataset into training set and test set\n"
				+ "train_ind = sample(1:nrow(dataset), size = nrow(dataset)*0.7)\n"
				+ "\n"
				+ "train = dataset[train_ind, ]\n"
				+ "X_test = dataset[-train_ind, -which(colnames(dataset) ==\""+target+"\")]\n"
				+ "y_test = as.factor(dataset[-train_ind, which(colnames(dataset) ==\""+target+"\")])\n"
				+ "\n"
				+ "model = rpart(formula = "+target+"~., data = train)\n"
				+ "\n"
				+ "pred = predict(model, X_test, type = 'class')\n"
				+ "\n"
				+ "acc = sum(pred == y_test)/length(y_test)\n"
				+ "print(acc)\n"
				+ "";
		
		// serialize code into Python filename
		
		Files.write(Rcode.getBytes(), new File(R_OUTPUT));



	}

	public void run() throws IOException {
		// execute the generated Python code
		// roughly: exec "python foofile.py"
		Process p = Runtime.getRuntime().exec("R -f " + R_OUTPUT);
	
		// output
		BufferedReader stdInput = new BufferedReader(new 
				InputStreamReader(p.getInputStream()));
	
		// error
		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(p.getErrorStream()));
	
		String o;
		while ((o = stdInput.readLine()) != null) {
			System.out.println(o);
		}
	
		String err; 
		while ((err = stdError.readLine()) != null) {
			System.out.println(err);
		}

	}

}
