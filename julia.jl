using DataFrames
using DecisionTree
using CSV
using StatsBase
using ScikitLearn
using DecisionTree
using Random
using MLBase

df = DataFrame(CSV.File("iris.csv"))

rename!(df,[:sepal_length, :sepal_width,:petal_length, :petal_width, :variety])


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
println(length(Y_train))
println(length(Y_test))

y_train = Int64[]
for i in 1:length(Y_train)
    if Y_train[i] === "Setosa"
        append!(y_train, 0)
    end
    if Y_train[i] === "Versicolor"
        append!(y_train, 1)
    end
    if Y_train[i] === "Virginica"
        append!(y_train, 2)
    end
end

y_test = Int64[]
for i in 1:length(Y_test)
    if Y_test[i] === "Setosa"
        append!(y_test, 0)
    end
    if Y_test[i] === "Versicolor"
        append!(y_test, 1)
    end
    if Y_test[i] === "Virginica"
        append!(y_test, 2)
    end
end

X_train = Matrix(X_train)
y_train = Array(y_train)
X_test = Matrix(X_test)
y_test = Array(y_test)

# train depth-truncated classifier
max_depth = 5
model = DecisionTreeClassifier(max_depth=max_depth)
using ScikitLearn: fit!
fit!(model, X_train, y_train)
# using ScikitLearn: predict
# predict(model, [5.9,3.0,5.1,1.9])
# get the probability of each label
# predict_proba(model, [5.9,3.0,5.1,1.9])
# println(get_classes(model)) # returns the ordering of the columns in predict_proba's output
# run n-fold cross validation over 3 CV folds
# See ScikitLearn.jl for installation instructions
using ScikitLearn.CrossValidation: cross_val_score
accuracy = cross_val_score(model, X_test, y_test, cv=3)
mean_accuracy = mean(accuracy)

using ScikitLearn: predict
pred = predict(model, X_test)

using MLBase: roc
r = roc(y_test, pred)

recall = recall(r)

precision = precision(r)

f1score = f1score(r)