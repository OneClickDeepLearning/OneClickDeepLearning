from Template.Layers.bn_relu import bn_relu
from keras.layers import Conv2D, add
def ResBlock_preAct_3layers(layer, filters, kernels, strides, activation, dropout=0, channel_change=False):
    # ↱------------------------------------↴
    # -BN-Act-Conv-BN-Act-Conv-BN-Act-Conv--
    #        ↳------------------------Conv-↑

    filter1, filter2, filter3 = filters
    kernel1, kernel2, kernel3 = kernels
    stride1, stride2, stride3 = strides

    shortcut = layer
    # BN1
    layer = bn_relu(layer, dropout=dropout, conv_activation=activation)

    if channel_change:

        shortcut = Conv2D(filters=filter3,
                       kernel_size=kernel1,
                       kernel_initializer='random_uniform',
                       # kernel_regularizer=regularizers.l2(0.01),
                       strides=stride1,
                       padding='same')(layer)

    layer = Conv2D(filters=filter1,
                   kernel_size=kernel1,
                   kernel_initializer='random_uniform',
                   # kernel_regularizer=regularizers.l2(0.01),
                   strides=stride1,
                   padding='same')(layer)

    layer = bn_relu(layer, dropout=dropout, conv_activation=activation)

    layer = Conv2D(filters=filter2,
                   kernel_size=kernel2,
                   kernel_initializer='random_uniform',
                   # kernel_regularizer=regularizers.l2(0.01),
                   strides=stride2,
                   padding='same')(layer)

    layer = bn_relu(layer, dropout=dropout, conv_activation=activation)

    layer = Conv2D(filters=filter3,
                   kernel_size=kernel3,
                   kernel_initializer='random_uniform',
                   # kernel_regularizer=regularizers.l2(0.01),
                   strides=stride3,
                   padding='same')(layer)

    output = add([shortcut, layer])

    return output