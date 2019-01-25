from model_adaptor import IModelAdaptor
from keras import models
import numpy
import json


class ModelKerasAdaptor(IModelAdaptor):
    def __init__(self):
        super().__init__()

    def load(self, model_file_path):
    	self.classifier_model = models.load_model(model_file_path)


    def predict(self, in_obj):
        return self.classifier_model.predict(in_obj)
        

    def file_postfix(self):
        return ".h5py"


    def serialize(self, out_obj):
        return json.dumps(numpy.tolist(out_obj))


    def deserialize(self, in_str):
        return numpy.asarray(json.loads(in_str))