from sklearn.externals import joblib
from model_adaptor import IModelAdaptor
import json


class ModelSklearnAdaptor(IModelAdaptor):
	def __init__(self):
		super().__init__()


	def load(self, model_file_path):
		self.classifier_model = joblib.load(model_file_path)


	def predict(self, in_obj):
		return self.classifier_model.predict(in_obj)


	def file_postfix(self):
		return "joblib"


	def serialize(self, out_obj):
		return json.dumps(out_obj)


	def deserialize(self, in_str):
		return json.loads(in_str)