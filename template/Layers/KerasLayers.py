from keras.layers import Conv2D, BatchNormalization, Activation


def conv_bn_act(input, filters, kernel_size, strides, padding, activation):
    conv = Conv2D(filters=filters, kernel_size=kernel_size, strides=strides, padding=padding)(input)
    bn = BatchNormalization()(conv)
    relu = Activation(activation=activation)(bn)
    return relu


def bn_act_conv(input, filters, kernel_size, strides, padding, activation):
    bn = BatchNormalization()(input)
    relu = Activation(activation=activation)(bn)
    conv = Conv2D(filters=filters, kernel_size=kernel_size, strides=strides, padding=padding)(relu)
    return conv


def act_conv_bn(input, filters, kernel_size, strides, padding, activation):
    relu = Activation(activation=activation)(input)
    conv = Conv2D(filters=filters, kernel_size=kernel_size, strides=strides, padding=padding)(relu)
    bn = BatchNormalization()(conv)
    return bn


def conv_act(input, filters, kernel_size, strides, padding, activation):
    conv = Conv2D(filters=filters, kernel_size=kernel_size, strides=strides, padding=padding)(input)
    relu = Activation(activation=activation)(conv)
    return relu


def conv_bn(input, filters, kernel_size, strides, padding):
    conv = Conv2D(filters=filters, kernel_size=kernel_size, strides=strides, padding=padding)(input)
    bn = BatchNormalization()(conv)
    return bn


def bn_act(input, activation):
    bn = BatchNormalization()(input)
    relu = Activation(activation=activation)(bn)
    return relu


def act_conv(input, filters, kernel_size, strides, padding, activation):
    relu = Activation(activation=activation)(input)
    conv = Conv2D(filters=filters, kernel_size=kernel_size, strides=strides, padding=padding)(relu)
    return conv


F = [[1,2,3],[4,5,6]]