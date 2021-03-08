using DataFrames
using DecisionTree
using CSV
using StatsBase
using ScikitLearn
using DecisionTree
using Random

df = DataFrame(CSV.File("C:\\Users\\Alex\\Desktop\\3A\\Génie logiciel\\julia\\iris.csv"))

rename!(df,[:sepal_length, :sepal_width,:petal_length, :petal_width, :variety])


# split the dataset 
sample = randsubseq(1:size(df,1), 0.8)
train = df[sample, :]
notsample = [i for i in 1:size(df,1) if isempty(searchsorted(sample, i))]
test = df[notsample, :]

col = names(df)
X_names = setdiff(col, ["variety"])

X_train = train[:, X_names]
Y_train = train[:, :variety]
X_test = test[:, X_names]
Y_test = test[:, :variety]

X_train = Matrix(X_train)
Y_train = Array(Y_train)
X_test = Matrix(X_test)
Y_test = Array(Y_test)

# train depth-truncated classifier
model = DecisionTreeClassifier(max_depth=2)
using ScikitLearn: fit!
fit!(model, X_train, Y_train)
# pretty print of the tree, to a depth of 5 nodes (optional)
print_tree(model, 5)
# apply learned model
using ScikitLearn: predict
predict(model, [5.9,3.0,5.1,1.9])
# get the probability of each label
predict_proba(model, [5.9,3.0,5.1,1.9])
println(get_classes(model)) # returns the ordering of the columns in predict_proba's output
# run n-fold cross validation over 3 CV folds
# See ScikitLearn.jl for installation instructions
using ScikitLearn.CrossValidation: cross_val_score
accuracy = cross_val_score(model, X_test, Y_test, cv=3)
mean_accuracy = mean(accuracy)