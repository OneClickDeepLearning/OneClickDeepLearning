from abc import abstractmethod


class TrainAdaptor():
    def __init__(self):
        pass

    @abstractmethod
    def process_data(self):
        pass

    @abstractmethod
    def train(self):
        pass

    @abstractmethod
    def save(self):
        pass
