from abc import abstractmethod


class ModelAdaptor():
	"""docstring for ModelAdaptor"""
	def __init__(self):
		pass
	@abstractmethod
	def get_Y(self):
		pass

	@abstractmethod
	def get_pred_score(self):
		pass

	@abstractmethod
	def get_pred_class(self):
		pass