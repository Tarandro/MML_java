library(rpart)
library(MLmetrics, quietly = TRUE, warn.conflicts = FALSE)

dataset = read.csv('iris.csv')
dataset[,'variety'] = as.factor(dataset[,'variety'])

# Spliting dataset into training set and test set
train_ind = sample(1:nrow(dataset), size = nrow(dataset)*0.3)

train = dataset[train_ind, ]
X_test = dataset[-train_ind, -which(colnames(dataset) =="variety")]
y_test = as.factor(dataset[-train_ind, which(colnames(dataset) =="variety")])

model = rpart(formula = variety~., data = train, control = rpart.control(maxdepth =5))

pred = predict(model, X_test, type = 'class')

score = "accuracy" 
acc = sum(pred == y_test)/length(y_test)
precision = Precision(y_test, pred) 
if (score == 'accuracy') {print(paste('accuracy :',acc))} else if (score == 'precision') {print(paste('precision :',precision))} 
