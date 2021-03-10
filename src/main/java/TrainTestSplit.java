import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import org.json.JSONObject;

import java.util.LinkedList;

import com.google.common.io.Files;

public class TrainTestSplit extends MLExecutor {
	
	private final String PYTHON_OUTPUT = "./script/traintestsplit.py";
	
	public TrainTestSplit(ConfigurationML configuration) {
		this.configuration = configuration;
	}

	// TODO: refactoring of the code is needed since anti-pattern/bad smell https://fr.wikipedia.org/wiki/Code_smell#Long_Parameter_List
	public void generateCode() throws IOException {
		
		String file_path = configuration.getFilePath();
		float train_size = configuration.getTrainSize();
		
				
		// Python code 
		String pythonCode = "import pandas as pd\n"
	             + "from sklearn.model_selection import train_test_split\n"
	             + "from sklearn import tree\n"
	             + "from sklearn.metrics import classification_report, confusion_matrix\n"
	             + "\n"
	             + "# Using pandas to import the dataset and sample\n"
	             + "df = pd.read_csv(\"" + file_path + "\")\n"
	             + "df = df.sample(n = len(df), random_state = 15).reset_index(drop=True)\n"
	             + "\n"
	             + "# Spliting dataset into training set and test set\n"
	             + "train_size = int(len(df)*" + train_size + ")\n"
	             + "train_dataset = df.iloc[:train_size].copy()\n"
	             + "test_dataset = df.iloc[train_size:].reset_index(drop=True).copy()\n"
	             + "\n"
	             + "file_path = '" + file_path + "'\n"
	             + "train_dataset.to_csv(file_path.replace('.csv','_train.csv'),index=False)\n"
	             + "test_dataset.to_csv(file_path.replace('.csv','_test.csv'),index=False)\n"
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
		
		JSONObject json_result = new JSONObject(); 

		String err; 
		while ((err = stdError.readLine()) != null) {
			result += err;
			// System.out.println(err);
		}
		
		return new MLResult(result, json_result);

	}
}
