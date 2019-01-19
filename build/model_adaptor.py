from abc import abstractmethod


class IModelAdaptor:
	def __init__(self):
		self.classifier_model = None

	@abstractmethod
	def load(self, model_file_path):
		pass

	@abstractmethod
	def predict(self, in_obj):
		pass

	@abstractmethod
	def file_postfix(self):
		pass

	@abstractmethod	
	def serialize(self, out_obj):
		pass

	@abstractmethod	
	def deserialize(self, in_str):
		pass