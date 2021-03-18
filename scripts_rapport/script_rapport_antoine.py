import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

data = pd.read_json('/home/antoine/Documents/GL/MML_java/benchmark/antoine/log_results_iris_10.json')
data = data.transpose()
data = data.reset_index()
data = data.drop(['index'], axis=1)
print(data.columns)

max_depth_unique = np.unique(data['max_depth'].values)
training_unique = np.unique(data['training'].values)
variant_unique = np.unique(data['variant'].values)

to_csv = pd.DataFrame(data=None, columns=['variant', 'training', 'max_depth', 'mean_time_ms', 'mean_accuracy', 'mean_precision', 'mean_recall', 'mean_f1'])

for variant in variant_unique :
    for training in training_unique:
        for depth in max_depth_unique:
            subsample = data[(data['variant'] == variant) & (data['training'] == training) & (data['max_depth'] == depth)]
            to_csv = to_csv.append({'variant': variant,
                                'training': np.round(training, decimals=2), 'max_depth': depth, 'mean_time_ms': np.round(np.mean(subsample['time_ms']), 0), 'mean_accuracy': np.round(np.mean(subsample['accuracy']),3) , 'mean_precision': np.round(np.mean(subsample['macro_precision']),3), 'mean_recall': np.round(np.mean(subsample['macro_recall']), 3), 'mean_f1': np.round(np.mean(subsample['macro_f1']), 3)}, ignore_index=True)
            print('lang: {} trn: {} dpth: {} mean_time_ms: {} mean_acc: {} mean_prec: {} mean_rec: {} mean_f1: {}'.format(variant,
                                np.round(training, decimals=2), depth, np.round(np.mean(subsample['time_ms']), 0), np.round(np.mean(subsample['accuracy']),3) , np.round(np.mean(subsample['macro_precision']),3), np.round(np.mean(subsample['macro_recall']), 3), np.round(np.mean(subsample['macro_f1']), 3)))

#to_csv.to_csv('/home/antoine/Documents/GL/MML_java/benchmark/antoine/iris_10_res.csv', index=False)
for metric in ['mean_time_ms', 'mean_accuracy']:
    for training in [0.5,0.7,0.9]:
        for depth in max_depth_unique:
            subsample2 = to_csv[
                    (to_csv['max_depth'] == depth) & (to_csv['training'] == training)]
            print(subsample2)
            if depth==5:
                ser_5 = subsample2[metric].values
            elif depth==10:
                ser_10 = subsample2[metric].values
            elif depth==20:
                ser_20 = subsample2[metric].values
                print(ser_5)
        index = ['Julia', 'Python', 'R']

        df = pd.DataFrame({'Depth 5': ser_5,
                           'Depth 10': ser_10, 'Depth 20': ser_20}, index=index)
        title_p = '{} for training ratio {}'.format(metric, training)
        ax = df.plot.bar(title= 'train {} metric {}'.format(training, metric),rot=0, color={"Depth 5": "green", "Depth 10": "blue", 'Depth 20': "red"})
        save_title = '{}_on_tr{}'.format(metric, training*100)
        plt.savefig('/home/antoine/Documents/GL/MML_java/benchmark/antoine/'+save_title+'.png')
        plt.show()



