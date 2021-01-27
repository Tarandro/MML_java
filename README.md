# multi machine learning (MML)

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

(TODO)

### Docker 

Docker files and images are provided to use MML. 

