import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn import tree
from sklearn.metrics import accuracy_score
from sklearn.metrics import precision_score

# Using pandas to import the dataset
df = pd.read_csv("iris.csv")

# Learn more on pandas read_csv :
#     https://pandas.pydata.org/pandas-docs/stable/reference/api/pandas.read_csv.html
# pandas input in general :
#     https://pandas.pydata.org/pandas-docs/stable/reference/io.html


# Spliting dataset between features (X) and label (y)
X = df.drop(columns=["varietyyy"])
y = df["varietyyy"]

# pandas dataframe operations :
#     https://pandas.pydata.org/pandas-docs/stable/reference/frame.html


# Spliting dataset into training set and test set
test_size = 1 - 0.5
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)

# scikit-learn train_test_split :
#     https://scikit-learn.org/stable/modules/generated/sklearn.model_selection.train_test_split.html
# Other model selection functions :
#     https://scikit-learn.org/stable/modules/classes.html#module-sklearn.model_selection


max_depth = 5
# Set algorithm to use
clf = tree.DecisionTreeClassifier(max_depth = max_depth)
print(clf)
# scikit-learn DecisionTreeClassifier :
#     https://scikit-learn.org/stable/modules/generated/sklearn.tree.DecisionTreeClassifier.html#sklearn.tree.DecisionTreeClassifier
# Other scikit-learn tree algorithms :
#     https://scikit-learn.org/stable/modules/classes.html#module-sklearn.tree

# Use the algorithm to create a model with the training set
clf.fit(X_train, y_train)

score = "accuracy" 
# Compute and display the accuracy
accuracy = accuracy_score(y_test, clf.predict(X_test))
precision = precision_score(y_test, clf.predict(X_test), average = 'weighted') 

if score == 'accuracy': print('accuracy:' + str(accuracy))

elif score == 'precision': print('precision : ' + str(precision))
# scikit-learn accuracy_score :
#     https://scikit-learn.org/stable/modules/generated/sklearn.metrics.accuracy_score.html
# Other scikit-learn metrics :
#     https://scikit-learn.org/stable/modules/classes.html#module-sklearn.metrics
