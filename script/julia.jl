using DataFrames
using DecisionTree
using CSV
using StatsBase
using ScikitLearn
using DecisionTree
using Random
using EvalMetrics
using MLLabelUtils
file_path = "./dataset/churn_dataset.csv"
df_train = DataFrame(CSV.File(replace(file_path, ".csv" => "_train.csv")))
df_test = DataFrame(CSV.File(replace(file_path, ".csv" => "_test.csv")))

col = names(df_train)
X_names = setdiff(col, ["Exited"])

X_train = df_train[:, X_names]
Y_train = df_train[:, :"Exited"]
X_test = df_test[:, X_names]
Y_test = df_test[:, :"Exited"]
Y_enc = labelenc(df_train[:, :"Exited"])
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
