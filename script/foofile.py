import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn import tree
from sklearn.metrics import classification_report, confusion_matrix

# Using pandas to import the dataset
file_path = './dataset/irist.csv'
df_train = pd.read_csv(file_path.replace('.csv','_train.csv'))
df_test = pd.read_csv(file_path.replace('.csv','_test.csv'))

# Spliting dataset between features (X) and label (y)
X_train = df_train.drop(columns=["variety"])
y_train = df_train["variety"]
X_test = df_test.drop(columns=["variety"])
y_test = df_test["variety"]

max_depth = 5
# Set algorithm to use
clf = tree.DecisionTreeClassifier(max_depth = max_depth)
# Use the algorithm to create a model with the training set
clf.fit(X_train, y_train)

metrics = ['macro_recall','macro_f1','accuracy','macro_precision'] 
# Prediction : 
pred = clf.predict(X_test) 
cm = confusion_matrix(y_test, pred, labels=y_test.unique()) 
report = classification_report(y_test, pred, digits = 4, output_dict = True)

accuracy = report['accuracy']
macro_precision = report['macro avg']['precision']
macro_recall = report['macro avg']['recall']
macro_f1 = report['macro avg']['f1-score']

if 'confusion' in metrics: print('confusion matrix:')
if 'confusion' in metrics: print(pd.DataFrame(cm, columns = y_test.unique(), index = y_test.unique()))
if 'accuracy' in metrics: print('accuracy:' + str(accuracy)) 
if 'macro_precision' in metrics: print('macro precision : ' + str(macro_precision)) 
if 'macro_recall' in metrics: print('macro recall : ' + str(macro_recall)) 
if 'macro_f1' in metrics: print('macro f1 : ' + str(macro_f1)) 
if 'weighted_precision' in metrics: print('weighted precision : ' + str(report['weighted avg']['precision'])) 
if 'weighted_recall' in metrics: print('weighted recall : ' + str(report['weighted avg']['recall'])) 
if 'weighted_f1' in metrics: print('weighted f1 : ' + str(report['weighted avg']['f1-score'])) 
if 'precision' in metrics: print(pd.DataFrame([report[label]['precision'] for label in y_test.unique()], columns = ['precision'], index = y_test.unique())) 
if 'recall' in metrics: print(pd.DataFrame([report[label]['recall'] for label in y_test.unique()], columns = ['recall'], index = y_test.unique())) 
if 'f1' in metrics: print(pd.DataFrame([report[label]['f1-score'] for label in y_test.unique()], columns = ['f1-score'], index = y_test.unique())) 
print('Metrics : accuracy:',accuracy, '/ macro_precision:',macro_precision, '/ macro_recall:',macro_recall, '/ macro_f1:',macro_f1)
