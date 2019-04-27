import sys
import os
import psutil

import matplotlib.image as mpimg
import matplotlib.pyplot as plt
import tensorflow as tf
import numpy as np
import skimage.transform as trans
from keras import backend as keras

def jaccard_distance(y_true, y_pred, smooth=1):
    intersection = keras.sum(keras.abs(y_true * y_pred), axis=-1)
    sum_ = keras.sum(keras.abs(y_true) + keras.abs(y_pred), axis=-1)
    jac = (intersection + smooth) / (sum_ - intersection + smooth)
    return (1 - jac) * smooth

def getMemCpu():
    info = psutil.virtual_memory()
    print("Memory use：")
    print(psutil.Process(os.getpid()).memory_info().rss)
    print("Total memory：")
    print(info.total)
    print("Memory percentage：")
    print(info.percent)
    print("Num of cpu：")
    print(psutil.cpu_count())


model_path = sys.argv[1]
test_pic_path = sys.argv[2]
output_image_path = sys.argv[3]
ground_truth_path = sys.argv[4]

# Load TFLite model and allocate tensors.
tflite_model = tf.contrib.lite.Interpreter(model_path=model_path)
tflite_model.allocate_tensors()

# Get input and output tensors.
input_details = tflite_model.get_input_details()
print(str(input_details))
output_details = tflite_model.get_output_details()
print(str(output_details))

# Test model on random input data.
test_pic = mpimg.imread(test_pic_path)
test_pic = test_pic / 255
test_pic = trans.resize(test_pic,(768,1024))


input_image = test_pic.reshape(-1,768,1024,3)
input_image = input_image.astype('float32')
print("Before:")
getMemCpu()
tflite_model.set_tensor(input_details[0]['index'], input_image)

tflite_model.invoke()

output_data = tflite_model.get_tensor(output_details[0]['index'])
print("After:")
getMemCpu()
print(output_data.shape)

output_image = output_data.reshape(768,1024) * 255
print(output_image.shape)

plt.imshow(output_image)

if os.path.exists(ground_truth_path):

    ground_truth = mpimg.imread(ground_truth_path)
    ground_truth = trans.resize(ground_truth, (768,1024))
    eval = jaccard_distance(output_data.reshape(768,1024), ground_truth.astype('float32'))

    sess = tf.Session()
    with sess.as_default():
        eval = np.average(eval.eval())

    plt.title('Jaccord Index: '+str(eval))

plt.savefig(output_image_path)


