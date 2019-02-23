from keras.layers import Conv2D, Activation,GlobalAveragePooling2D
def global_average_pooling(layer, cls):
    layer = Conv2D(cls, [1, 1])(layer)
    layer = GlobalAveragePooling2D()(layer)
    layer = Activation(activation='softmax')(layer)
    return layer