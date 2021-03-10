import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import org.json.JSONObject;

import java.util.LinkedList;

import com.google.common.io.Files;

public class PythonMLExecutor extends MLExecutor {
	
	private final String PYTHON_OUTPUT = "./script/foofile.py";
	
	public PythonMLExecutor(ConfigurationML configuration) {
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
		String pythonCode = "import pandas as pd\n"
	             + "from sklearn.model_selection import train_test_split\n"
	             + "from sklearn import tree\n"
	             + "from sklearn.metrics import classification_report, confusion_matrix\n"
	             + "\n"
	             + "# Using pandas to import the dataset\n"
	             + "file_path = '" + file_path + "'\n"
	             + "df_train = pd.read_csv(file_path.replace('.csv','_train.csv'))\n"
	             + "df_test = pd.read_csv(file_path.replace('.csv','_test.csv'))\n"
	             + "\n"
	             + "# Spliting dataset between features (X) and label (y)\n"
	             + "X_train = df_train.drop(columns=[\"" + target + "\"])\n"
	             + "y_train = df_train[\"" + target + "\"]\n"
	             + "X_test = df_test.drop(columns=[\"" + target + "\"])\n"
	             + "y_test = df_test[\"" + target + "\"]\n"
	             + "\n"
	             + "max_depth = " + max_depth + "\n"
	             + "# Set algorithm to use\n"
	             + "clf = tree.DecisionTreeClassifier(max_depth = max_depth)\n"
	             + "# Use the algorithm to create a model with the training set\n"
	             + "clf.fit(X_train, y_train)\n"
	             + "\n"
	             + "metrics = ['" + String.join("','", metrics) + "'] \n"
	             + "# Prediction : \n"
	             + "pred = clf.predict(X_test) \n"
	             + "cm = confusion_matrix(y_test, pred, labels=y_test.unique()) \n"
	             + "report = classification_report(y_test, pred, digits = 4, output_dict = True)\n"
	             + "\n"
	             + "accuracy = report['accuracy']\n"
	             + "macro_precision = report['macro avg']['precision']\n"
	             + "macro_recall = report['macro avg']['recall']\n"
	             + "macro_f1 = report['macro avg']['f1-score']\n"
	             + "\n"
	             + "if 'confusion' in metrics: print('confusion matrix:')\n"
	             + "if 'confusion' in metrics: print(pd.DataFrame(cm, columns = y_test.unique(), index = y_test.unique()))\n"
	             + "if 'accuracy' in metrics: print('accuracy:' + str(accuracy)) \n"
	             + "if 'macro_precision' in metrics: print('macro precision : ' + str(macro_precision)) \n"
	             + "if 'macro_recall' in metrics: print('macro recall : ' + str(macro_recall)) \n"
	             + "if 'macro_f1' in metrics: print('macro f1 : ' + str(macro_f1)) \n"
	             + "if 'weighted_precision' in metrics: print('weighted precision : ' + str(report['weighted avg']['precision'])) \n"
	             + "if 'weighted_recall' in metrics: print('weighted recall : ' + str(report['weighted avg']['recall'])) \n"
	             + "if 'weighted_f1' in metrics: print('weighted f1 : ' + str(report['weighted avg']['f1-score'])) \n"
	             + "if 'precision' in metrics: print(pd.DataFrame([report[label]['precision'] for label in y_test.unique()], columns = ['precision'], index = y_test.unique())) \n"
	             + "if 'recall' in metrics: print(pd.DataFrame([report[label]['recall'] for label in y_test.unique()], columns = ['recall'], index = y_test.unique())) \n"
	             + "if 'f1' in metrics: print(pd.DataFrame([report[label]['f1-score'] for label in y_test.unique()], columns = ['f1-score'], index = y_test.unique())) \n"
	             + "print('Metrics : accuracy:',accuracy, '/ macro_precision:',macro_precision, '/ macro_recall:',macro_recall, '/ macro_f1:',macro_f1)\n"
	             + "";

		// serialize code into Python filename
				
		Files.write(pythonCode.getBytes(), new File(PYTHON_OUTPUT));


	}

	public MLResult run() throws IOException {
		String target = configuration.getTarget();
		
		// execute the generated Python code
		// roughly: exec "python foofile.py"
		Process p = Runtime.getRuntime().exec("python3 " + PYTHON_OUTPUT);
	
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
