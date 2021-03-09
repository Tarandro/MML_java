using DataFrames
using DecisionTree
using CSV
using StatsBase
using ScikitLearn
using DecisionTree
using Random
using EvalMetrics
using MLLabelUtils
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
Y_enc = labelenc(df[:, :variety])
y_train = Int64[]
for i in 1:length(Y_train)
    append!(y_train, label2ind(Y_train[i], Y_enc))
end
y_test = Int64[]
for i in 1:length(Y_test)
    append!(y_test, label2ind(Y_test[i], Y_enc))
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
print("accuracy : "*string(mean_accuracy)*"
")

using ScikitLearn: predict
pred = predict(model, X_test)

using EvalMetrics:ConfusionMatrix
using EvalMetrics:precision
using EvalMetrics:recall
using EvalMetrics:f1_score

recall_list = Float64[]
precision_list = Float64[]
f1score_list = Float64[]
for i in unique(y_test)
    predi = ifelse.(pred .== i, 1, 0)
    y_testi = ifelse.(y_test .== i, 1, 0)
    cmi = ConfusionMatrix(y_testi, predi)
    append!(recall_list, recall(cmi))
    append!(precision_list, precision(cmi))
    append!(f1score_list, f1_score(cmi))
end

macro_recall = mean(recall_list)
print("macro_recall : "*string(macro_recall)*"
")

macro_precision = mean(precision_list)
print("macro_precision : "*string(macro_precision)*"
")

macro_f1 = mean(f1score_list)
print("macro_f1 : "*string(macro_f1)*"
")
print("Metrics : accuracy: "*string(mean_accuracy)*" / macro_precision: "*string(macro_precision)*" / macro_recall: "*string(macro_recall)*" / macro_f1: "*string(macro_f1))
