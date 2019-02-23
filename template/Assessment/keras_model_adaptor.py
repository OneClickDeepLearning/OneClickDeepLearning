from keras import models
import numpy as np
from Template.Assessment.model_adaptor import ModelAdaptor
import time


class KerasModelAdaptor(ModelAdaptor):
    """docstring for SklearnModelAdaptor"""

    def __init__(self, model_file_path, x_test, y_test, input_shape):
        shape = input_shape
        self.model = models.load_model(model_file_path)
        self.y_file_path = y_test
        self.x_file_path = x_test
        shape = shape.insert(0,-1)
        self.shape = shape
        pass

    def get_pred_score(self):
        x_test = np.load(self.x_file_path)
        x_test = x_test.reshape(self.shape)
        return self.model.predict(x_test)

    def get_pred_class(self):
        x_test = np.load(self.x_file_path)
        x_test = x_test.reshape(self.shape)
        try:
            pred = self.model.predict_classes(x_test)
            return pred
        except:
            pred = self.model.predict(x_test)
            return np.argmax(pred, axis=1)

    def get_Y(self):
        y_label = np.load(self.y_file_path)
        try:
            return y_label[:, 0]
        except:
            return y_label
