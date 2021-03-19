# multi machine learning (MML)

TODO rendu collectif:

- instructions + requirement packages dans readme 


- site web


The goal of this project is to provide high-level facilities to perform machine learning tasks (e.g classification).
The user "just" has to specify a configuration file (in e.g JSON) with information about the dataset, the predictive variables, etc. 
Then, her specification is compiled in different languages/libraries and can then be executed. 
In a sense, doing machine learning in a declarative way, without fighting with the cryptic details of off-the-self libraries. 

## Java implementation

The processing of configuration files and compilers are written in Java. 

### Python

Python 3 should be installed with the pandas and scikit-learn library: 
`pip install -r requirements.txt`

warning: on Windows system, the `python` command can well be `py` 

### R

`R` should be installed with `rpart` library

### Julia

Julia should be installed and requires the following packages: 

- DataFrames
- DecisionTree
- CSV
- StatsBase
- ScikitLearn
- DecisionTree
- Random
- MLMetrics

With the interactive mode of Julia, use `using Pkg()` then `Pkg.add(<package name>)` for each package to install them.


### Maven

(Commands are for Linux)

To perform tests with JUnit use `mvn test` in the root folder of this project.

To package the project as an executable JAR use `mvn package` in the root folder of this project. Then get the JAR file in the `/target` folder.
This JAR file can be executed with the command `java -jar <filepath>`.

### How to use the application

- First launch the server. There are two ways to do this
	- use Eclipse or another IDE to open the project and run the class "ServerMain"
	- package the project using Maven as indicated above, then run the JAR file

- Then open the `index.html` file in your browser.

- Once you are on the webpage, you can upload a JSON file which matches this kind of format (do not use it as it is, this is just an example, delete # lines first) :

CAUTION: the following parameters which are lists work serverside (they all the results) with several elements, however the web app only handles one result, so you should use one-element lists when it is recommended in order to use the web app correctly.


{ <br />
 "dataset": { <br />
	"filename" : "<path to your csv file>", #path to your csv file <br />
   "separator" : "," #enter your separator here <br />
 }, <br />
 "training": [0.7], #or any list of training ratios in ]0,1[, ONE ELEMENT ONLY IS RECOMMENDED <br />
 "target_variable": "variety", #you target variable <br />
 "metrics" : ['accuracy', 'confusion', 'macro_precision', 'macro_recall', 'macro_f1'], #you can select a sublist of this one <br />
 "max_depth" : [5], #or any list of integers above 1, ONE ELEMENT ONLY IS RECOMMENDED <br />
 "languages" : ["Python", "R", "Julia"], #you can take a sublist of this one, ONE ELEMENT ONLY IS RECOMMENDED <br />
 "repetition" : 1 #or any integer <br />
} <br />


If you lack inspiration, you can try `file_to_upload.json`

- Then click on the "Upload file" button and wait until "Success" is displayed.

- Finally, click on "Open result" to print the result.

- Click on the cross to close the window and delete the uploaded JSON.

- Shut the server down.

