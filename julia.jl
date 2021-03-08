using DataFrames
using DecisionTree
using CSV
using StatsBase
using ScikitLearn
using Random
df = DataFrame(CSV.File("iris.csv"))

# split the dataset 
sample = randsubseq(1:size(df,1), 0.8)
train = df[sample, :]
notsample = [i for i in 1:size(df,1) if isempty(searchsorted(sample, i))]
test = df[notsample, :]
col = names(df)
X_names = setdiff(col, ["variety"])
X_train = train[:, X_names]
Y_train = train[:, :"variety"]
X_test = test[:, X_names]
Y_test = test[:, :"variety"]

X_train = Matrix(X_train)
Y_train = Array(Y_train)
X_test = Matrix(X_test)
Y_test = Array(Y_test)

# train depth-truncated classifier
max_depth = 5
model = DecisionTreeClassifier(max_depth=max_depth)
using ScikitLearn: fit!
fit!(model, X_train, Y_train)
# run n-fold cross validation over 3 CV folds
# See ScikitLearn.jl for installation instructions
using ScikitLearn.CrossValidation: cross_val_score
accuracy = cross_val_score(model, X_test, Y_test, cv=3)
println(mean(accuracy))