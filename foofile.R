library(rpart)

dataset = read.csv('iris.csv')
dataset[,'variety'] = as.factor(dataset[,'variety'])

# Spliting dataset into training set and test set
train_ind = sample(1:nrow(dataset), size = nrow(dataset)*0.7)

train = dataset[train_ind, ]
X_test = dataset[-train_ind, -which(colnames(dataset) =="variety")]
y_test = as.factor(dataset[-train_ind, which(colnames(dataset) =="variety")])

model = rpart(formula = variety~., data = train, control = rpart.control(maxdepth =5))

pred = pred = as.vector(predict(model, X_test, type = 'class'))

metrics = c('precision','macro_recall','macro_f1','accuracy')

cm = as.matrix(table(Actual = y_test, Predicted = pred))
if ('confusion' %in% metrics) {print('confusion matrix :')
  print(cm)}
n = sum(cm)
nc = nrow(cm)
diag = diag(cm)
rowsums = apply(cm, 1, sum)
colsums = apply(cm, 2, sum)

acc = sum(diag) / n 
precision = diag / colsums 
recall = diag / rowsums 
f1 = 2 * precision * recall / (precision + recall) 
macroPrecision = mean(precision) 
macroRecall = mean(recall) 
macroF1 = mean(f1) 

if ('accuracy' %in% metrics) {print(paste('accuracy :',acc))} 
if ('macro_precision' %in% metrics) {print(paste('macro precision :',macroPrecision))} 
if ('macro_recall' %in% metrics) {print(paste('macro recall :',macroRecall))} 
if ('macro_f1' %in% metrics) {print(paste('macro f1 :',macroF1))} 
if ('precision' %in% metrics) {print(data.frame(precision))} 
if ('recall' %in% metrics) {print(data.frame(recall))} 
if ('f1' %in% metrics) {print(data.frame(f1))} 
