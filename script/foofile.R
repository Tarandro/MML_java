library(rpart)
file_path = './dataset/iris.csv'
df_train = read.csv(gsub('.csv','_train.csv',file_path))
df_train[,'variety'] = as.factor(df_train[,'variety'])
df_test = read.csv(gsub('.csv','_test.csv',file_path))
df_test[,'variety'] = as.factor(df_test[,'variety'])

X_test = df_test[, -which(colnames(df_test) =="variety")]
y_test = as.factor(df_test[, which(colnames(df_test) =="variety")])

model = rpart(formula = variety~., data = df_train, control = rpart.control(maxdepth =5))

pred = as.vector(predict(model, X_test, type = 'class'))

metrics = c('macro_recall','macro_f1','accuracy','macro_precision')

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
print(paste('Metrics : accuracy:',acc, '/ macro_precision:',macroPrecision, '/ macro_recall:',macroRecall, '/ macro_f1:',macroF1))
