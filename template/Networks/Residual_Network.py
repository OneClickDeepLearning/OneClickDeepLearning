from Template.Layers.global_average_pooling import global_average_pooling
from Template.Blocks.ResBlock_3layers import ResBlock_3layers
from keras.layers import Input, Conv2D, BatchNormalization, Activation
from keras import Model

def Res50(cls, input_shape):
    input = Input(shape=input_shape)
    conv1 = Conv2D(filters=64, kernel_size=[7,7], strides=[2,2], padding='same')(input)
    bn1 = BatchNormalization()(conv1)
    act1 = Activation('relu')(bn1)

    block1 = ResBlock_3layers(layer=act1, filters=(64, 64, 256), kernels=(1, 3, 1),
                              activation='relu', channel_change=True, strides = (1,1,1))
    block2 = ResBlock_3layers(layer=block1, filters=(64, 64, 256), kernels=(1, 3, 1),
                              activation='relu', channel_change=False, strides = (1,1,1))
    block3 = ResBlock_3layers(layer=block2, filters=(64, 64, 256), kernels=(1, 3, 1),
                              activation='relu', channel_change=False, strides = (1,1,1))

    block4 = ResBlock_3layers(layer=block3, filters=(128, 128, 512), kernels=(1, 3, 1),
                              activation='relu', channel_change=True, strides = (2,1,1))
    block5 = ResBlock_3layers(layer=block4, filters=(128, 128, 512), kernels=(1, 3, 1),
                              activation='relu', channel_change=False, strides = (1,1,1))
    block6 = ResBlock_3layers(layer=block5, filters=(128, 128, 512), kernels=(1, 3, 1),
                              activation='relu', channel_change=False, strides = (1,1,1))
    block7 = ResBlock_3layers(layer=block6, filters=(128, 128, 512), kernels=(1, 3, 1),
                              activation='relu', channel_change=False, strides = (1,1,1))

    block8 = ResBlock_3layers(layer=block7, filters=(256, 256, 1024), kernels=(1, 3, 1),
                              activation='relu', channel_change=True, strides = (2,1,1))
    block9 = ResBlock_3layers(layer=block8, filters=(256, 256, 1024), kernels=(1, 3, 1),
                              activation='relu', channel_change=False, strides = (1,1,1))
    block10 = ResBlock_3layers(layer=block9, filters=(256, 256, 1024), kernels=(1, 3, 1),
                               activation='relu', channel_change=False, strides = (1,1,1))
    block11 = ResBlock_3layers(layer=block10, filters=(256, 256, 1024), kernels=(1, 3, 1),
                               activation='relu', channel_change=False, strides = (1,1,1))
    block12 = ResBlock_3layers(layer=block11, filters=(256, 256, 1024), kernels=(1, 3, 1),
                               activation='relu', channel_change=False, strides = (1,1,1))
    block13 = ResBlock_3layers(layer=block12, filters=(256, 256, 1024), kernels=(1, 3, 1),
                               activation='relu', channel_change=False, strides = (1,1,1))

    block14 = ResBlock_3layers(layer=block13, filters=(512, 512, 2048), kernels=(1, 3, 1),
                               activation='relu', channel_change=True, strides = (2,1,1))
    block15 = ResBlock_3layers(layer=block14, filters=(512, 512, 2048), kernels=(1, 3, 1),
                               activation='relu', channel_change=False, strides = (1,1,1))
    block16 = ResBlock_3layers(layer=block15, filters=(512, 512, 2048), kernels=(1, 3, 1),
                               activation='relu', channel_change=False, strides = (1,1,1))

    output = global_average_pooling(block16,cls)
    model = Model(inputs=[input], outputs=[output])
    model.summary()
    return model

