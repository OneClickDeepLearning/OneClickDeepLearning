from abc import abstractmethod


class Hierarchical_Adaptor:
    def __init__(self):
        pass

    @abstractmethod
    def build_network(self):
        pass

    @abstractmethod
    def predict(self, models_all):
        pass
