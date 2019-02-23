import numpy as np
class HierAssessModel:
    def __init__(self,model_adaptor):
        self.model_adaptor = model_adaptor

    def hierarchical_assessment(self, n = 1):
        # pred_list: list of predicted label path       exp:[[4,121],[1,6],[2,36],[2,33]...]
        # real_list: list of real label path            exp:[[4,124],[1,6],[3,65],[2,33]...]
        # Returns hP, hR, F-n score                     dtype: float
        def sparse_label(input, bias = 0):
            for i in range(len(input)):
                input[i][1] = input[i][1] + input[i][0] * 1000 + bias
        pred_list = self.model_adaptor.predict()
        real_list = np.load(self.model_adaptor.y_file_path)
        sparse_label(pred_list)
        sparse_label(real_list)
        INTER = []
        PRED = []
        REAL = []
        for pred, real in zip(pred_list, real_list):
            inter = [i for i in pred if i in real]
            INTER.append(len(inter))
            PRED.append(len(pred))
            REAL.append(len(real))
        INTER = float(sum(INTER))
        PRED = float(sum(PRED))
        REAL = float(sum(REAL))
        hP = INTER / PRED
        hR = INTER / REAL
        Fn = ((n * n + 1) * hP * hR) / (n * n * hP + hR)

        count_all = 0
        count = 0
        for pred, real in zip(pred_list, real_list):
            if pred[1] == real[1]:
                count += 1
            count_all += 1
        acc = count / count_all

        return hP, hR, Fn, acc

