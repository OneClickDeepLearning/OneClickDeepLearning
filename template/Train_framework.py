from Template.Adaptors.trainKerasModel import TrainKerasModel
import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn import preprocessing
from keras.utils.np_utils import to_categorical

'''Initialize file path'''

X_train_path = '../data/train_x.npy'
Y_train_path = '../data/train_y.npy'
X_test = '../data/test_x.npy'
Y_test = '../data/test_y.npy'
save_path = '../models/testone'

'''Initialize training settings'''

epoch = 1
batch_size = 50
input_shape = [10000]
output_classes = 5
# optional, modify if you like
from keras.callbacks import EarlyStopping

call_backs = EarlyStopping(monitor='val_loss', min_delta=1e-3, patience=5, verbose=1, mode='auto')
loss = 'categorical_crossentropy'
optimizer = 'adam'
'''process data'''


def process_data(X_train_path, Y_train_path, test_size, classes):
    X_train = np.load(X_train_path)
    Y_train = np.load(Y_train_path)
    x_train, x_val, y_train, y_val = train_test_split(X_train, Y_train, test_size=test_size, random_state=42)
    y_train = pd.DataFrame(y_train)[0]
    y_val = pd.DataFrame(y_val)[0]
    y_labels = list(range(classes))
    le = preprocessing.LabelEncoder()
    le.fit(y_labels)
    y_train = to_categorical(y_train.map(lambda x: le.transform([x])[0]), classes)
    y_val = to_categorical(y_val.map(lambda x: le.transform([x])[0]), classes)
    return x_train, y_train, x_val, y_val

#
X_train, Y_train, X_val, Y_val = process_data(X_train_path, Y_train_path, test_size=0.2, classes= output_classes)

'''generalize network'''
from Template.Networks.NNnetwork import NNnetwork
from Template.Networks.Residual_Network import Res50
model = NNnetwork(output_classes,input_shape)
# model = Res50(output_classes,input_shape)

'''train model'''

import tensorflow as tf
tf.Session(config=tf.ConfigProto(log_device_placement=True))
train = TrainKerasModel(model=model,
                        X_train=X_train, Y_train=Y_train,
                        X_val=X_val, Y_val=Y_val,input_shape=input_shape)
train.train(batch_size=batch_size, epoch=epoch,
            call_backs=call_backs, loss=loss, optimizer=optimizer)
del X_train,Y_train,X_val,Y_val
train.save(save_path)
del train



'''assessment'''

from Template.Assessment.keras_model_adaptor import KerasModelAdaptor
from Template.Assessment.assess_model import AssessModel

AssessKeras = KerasModelAdaptor(model_file_path=save_path,
                                x_test=X_test,y_test=Y_test,
                                input_shape=input_shape)
Assessment = AssessModel(AssessKeras)
Assessment.draw_roc(output_classes)
Assessment.metrics()
