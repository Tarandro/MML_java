import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.common.io.Files;

public class PythonMLExecutor extends MLExecutor {
	
	private final String PYTHON_OUTPUT = "foofile.py";
	
	public PythonMLExecutor(ConfigurationML configuration) {
		this.configuration = configuration;
	}

	// TODO: refactoring of the code is needed since anti-pattern/bad smell https://fr.wikipedia.org/wiki/Code_smell#Long_Parameter_List
	public void generateCode() throws IOException {
		
		String file_path = configuration.getFilePath();
		String target = configuration.getTarget();
		
				
		// Python code 
		String pythonCode = "import pandas as pd\n"
				+ "from sklearn.model_selection import train_test_split\n"
				+ "from sklearn import tree\n"
				+ "from sklearn.metrics import accuracy_score\n"
				+ "\n"
				+ "# Using pandas to import the dataset\n"
				+ "df = pd.read_csv(\""+ file_path +"\")\n"
				+ "\n"
				+ "# Learn more on pandas read_csv :\n"
				+ "#     https://pandas.pydata.org/pandas-docs/stable/reference/api/pandas.read_csv.html\n"
				+ "# pandas input in general :\n"
				+ "#     https://pandas.pydata.org/pandas-docs/stable/reference/io.html\n"
				+ "\n"
				+ "\n"
				+ "# Spliting dataset between features (X) and label (y)\n"
				+ "X = df.drop(columns=[\""+target+"\"])\n"
				+ "y = df[\""+target+"\"]\n"
				+ "\n"
				+ "# pandas dataframe operations :\n"
				+ "#     https://pandas.pydata.org/pandas-docs/stable/reference/frame.html\n"
				+ "\n"
				+ "\n"
				+ "# Spliting dataset into training set and test set\n"
				+ "test_size = 0.3\n"
				+ "X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)\n"
				+ "\n"
				+ "# scikit-learn train_test_split :\n"
				+ "#     https://scikit-learn.org/stable/modules/generated/sklearn.model_selection.train_test_split.html\n"
				+ "# Other model selection functions :\n"
				+ "#     https://scikit-learn.org/stable/modules/classes.html#module-sklearn.model_selection\n"
				+ "\n"
				+ "\n"
				+ "# Set algorithm to use\n"
				+ "clf = tree.DecisionTreeClassifier()\n"
				+ "\n"
				+ "# scikit-learn DecisionTreeClassifier :\n"
				+ "#     https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeClassifier.html#sklearn.tree.DecisionTreeClassifier\n"
				+ "# Other scikit-learn tree algorithms :\n"
				+ "#     https://scikit-learn.org/stable/modules/classes.html#module-sklearn.tree\n"
				+ "\n"
				+ "# Use the algorithm to create a model with the training set\n"
				+ "clf.fit(X_train, y_train)\n"
				+ "\n"
				+ "# Compute and display the accuracy\n"
				+ "accuracy = accuracy_score(y_test, clf.predict(X_test))\n"
				+ "\n"
				+ "print(accuracy)\n"
				+ "\n"
				+ "# scikit-learn accuracy_score :\n"
				+ "#     https://scikit-learn.org/stable/modules/generated/sklearn.metrics.accuracy_score.html\n"
				+ "# Other scikit-learn metrics :\n"
				+ "#     https://scikit-learn.org/stable/modules/classes.html#module-sklearn.metrics\n"
				+ "";

		// serialize code into Python filename
				
		Files.write(pythonCode.getBytes(), new File(PYTHON_OUTPUT));


	}

	public MLResult run() throws IOException {
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
