from Template.Assessment.keras_model_adaptor import KerasModelAdaptor
class KerasHierModelAdaptor:
    def __init__(self, models_path, x_file_path,input_shape, y_file_path, network_size):
        # x(y)_file_path: test data path                Type: String
        # models_path: list of model paths              Type: String List   exp:['aaa.h5py','bbb.h5py'...]
        # Y_data label changed into label path          exp:[124,6,65,33]-->[[4,124],[1,6],[3,65],[2,33]]
        # for the first item [4,124], 4 is predict label for the 1st layer and 124 the 2nd layer.
        self.x_file_path = x_file_path
        self.y_file_path = y_file_path
        self.models_path = models_path
        # network_size: how many models for each layer  Type: List      exp: [1,5]
        # layer: how many layers                        Type: Int       exp: 2
        self.network_size = network_size
        self.layers = len(network_size)
        self.input_shape = input_shape

    def build_network(self):
        # instantiate models for each layer, return a list of models
        models_all = []
        for l in range(self.layers):
            models = []
            for i in range(self.network_size[l]):
                model_path = self.models_path.pop(0)
                model = KerasModelAdaptor(model_path,self.y_file_path,self.x_file_path,self.input_shape)
                models.append((l, i, model))
            models_all.append(models)
            self.models_list = models_all
        return models_all

    def predict(self):
        # Fit ALL data into ALL models, and returns list of predicted label path
        # Return                                       exp:[[4,121],[1,6],[2,35],[2,33]...]
        models_all = self.models_list

        predicts = []
        pred = []
        pred_this_layer = []
        for models in models_all:
            layer_index = models[0][0]
            if layer_index == 0:
                predicts.append(models[0][2].get_pred_class())
            elif layer_index != 0:
                for model in models:
                    pred.append(model[2].get_pred_class())

                for i in range(len(predicts[layer_index - 1])):
                    pred_this_layer.append(pred[predicts[layer_index - 1][i]][i])
                predicts.append(pred_this_layer)

            pred.clear()
            pred_this_layer = []

        pred_list = []
        for i in range(len(predicts[0])):
            predict = []
            for j in range(len(predicts)):
                predict.append(predicts[j][i])
            pred_list.append(predict)



        return pred_list

