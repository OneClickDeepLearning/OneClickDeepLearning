from abc import abstractmethod

class IModelAdaptor:
	def __init__(self):
		self.classifier_model = None

	@abstractmethod
	def load(self, model_file_path):
		pass

	@abstractmethod
	def predict(self, input):
		pass
