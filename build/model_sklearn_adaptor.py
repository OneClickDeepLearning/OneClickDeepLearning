from sklearn.externals import joblib
from model_adaptor import IModelAdaptor


class ModelSklearnAdaptor(IModelAdaptor):
	def __init__(self):
	    super().__init__()

	def load(self, model_file_path):
		self.classifier_model = joblib.load(model_file_path)


	def predict(self, input):
		self.classifier_model.predict(input)