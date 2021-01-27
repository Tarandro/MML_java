library(rpart)

dataset = read.csv('iris.csv')

# Spliting dataset into training set and test set
train_ind = sample(1:nrow(dataset), size = nrow(dataset)*0.7)

train = dataset[train_ind, ]
X_test = dataset[-train_ind, -which(colnames(dataset) =="variety")]
y_test = as.factor(dataset[-train_ind, which(colnames(dataset) =="variety")])

model = rpart(formula = variety~., data = train)

pred = predict(model, X_test, type = 'class')

acc = sum(pred == y_test)/length(y_test)
print(acc)
