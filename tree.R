library(rpart)

# download.file('https://raw.githubusercontent.com/vincentarelbundock/Rdatasets/master/csv/datasets/iris.csv', destfile = '~/Téléchargements/iris.csv')
iris = read.csv('iris.csv')
# iris = iris[,2:6]

train_ind = sample(1:nrow(iris), size = nrow(iris)*0.7)

train = iris[train_ind, ]
X_test = iris[-train_ind, -5]
y_test = as.factor(iris[-train_ind, 5])

model = rpart(formula = variety~., data = train)

pred = predict(model, X_test[,-5], type = 'class')

acc = sum(pred == y_test)/length(y_test)
print(acc)
