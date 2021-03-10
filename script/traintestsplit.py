import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn import tree
from sklearn.metrics import classification_report, confusion_matrix

# Using pandas to import the dataset and sample
df = pd.read_csv("./dataset/churn_dataset.csv")
df = df.sample(n = len(df), random_state = 15).reset_index(drop=True)

# Spliting dataset into training set and test set
train_size = int(len(df)*0.7)
train_dataset = df.iloc[:train_size].copy()
test_dataset = df.iloc[train_size:].reset_index(drop=True).copy()

file_path = './dataset/churn_dataset.csv'
train_dataset.to_csv(file_path.replace('.csv','_train.csv'),index=False)
test_dataset.to_csv(file_path.replace('.csv','_test.csv'),index=False)
