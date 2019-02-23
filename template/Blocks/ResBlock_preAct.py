from Template.Layers.bn_relu import bn_relu
from keras.layers import Conv2D, add

def ResBlock_preAct(layer, filters, kernels, strides, activation, dropout=0, channel_change=False):
    #             ↱-----------↴
    # -BN-Act-Conv-BN-Act-Conv-
    # ↳-----------------------↑

    filter1, filter2 = filters
    kernel1, kernel2 = kernels
    stride1, stride2 = strides

    shortcut = layer

    layer = bn_relu(layer, dropout=dropout, conv_activation=activation)

    layer = Conv2D(filters=filter1,
                   kernel_size=kernel1,
                   kernel_initializer='random_uniform',
                   # kernel_regularizer=regularizers.l2(0.01),
                   strides=stride1,
                   padding='same')(layer)

    if channel_change:
        shortcut = layer

    layer = bn_relu(layer, dropout=dropout, conv_activation=activation)

    layer = Conv2D(filters=filter2,
                   kernel_size=kernel2,
                   kernel_initializer='random_uniform',
                   # kernel_regularizer=regularizers.l2(0.01),
                   strides=stride2,
                   padding='same')(layer)

    output = add([shortcut, layer])

    return output


