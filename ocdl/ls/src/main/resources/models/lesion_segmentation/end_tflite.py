import sys

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

# print(tf.__version__)


# Load TFLite model and allocate tensors.
tflite_model = tf.contrib.lite.Interpreter(model_path=sys.argv[1])
# tflite_model = tf.contrib.lite.Interpreter(model_path="unet_membrane.tflite")
tflite_model.allocate_tensors()

# Get input and output tensors.
input_details = tflite_model.get_input_details()
print(str(input_details))
output_details = tflite_model.get_output_details()
print(str(output_details))

# Test model on random input data.
test_pic = mpimg.imread(sys.argv[2])
# test_pic = mpimg.imread('./ISIC_0000000.jpg')
test_pic = test_pic / 255
test_pic = trans.resize(test_pic,(768,1024))


input_image = test_pic.reshape(-1,768,1024,3)
input_image = input_image.astype('float32')
tflite_model.set_tensor(input_details[0]['index'], input_image)

tflite_model.invoke()
output_data = tflite_model.get_tensor(output_details[0]['index'])
print(output_data.shape)

output_image = output_data.reshape(768,1024) * 255
print(output_image.shape)

plt.imshow(output_image)

# ground_truth = mpimg.imread('./ISIC_0000000_segmentation.png')
ground_truth = mpimg.imread(sys.argv[4])
ground_truth = trans.resize(ground_truth, (768,1024))
eval = jaccard_distance(output_data.reshape(768,1024), ground_truth.astype('float32'))

sess = tf.Session()
with sess.as_default():
    eval = np.average(eval.eval())

plt.title('Jaccord Index: '+str(eval))
plt.savefig(sys.argv[3])


