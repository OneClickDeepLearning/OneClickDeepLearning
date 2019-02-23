from keras import Sequential
from keras.layers import Dense, Dropout

def NNnetwork(output_class,input_shape):
    model = Sequential()
    model.add(Dense(1024, input_shape=input_shape, activation='relu'))
    model.add(Dropout(0.2))
    model.add(Dense(256, activation='relu'))
    model.add(Dropout(0.2))
    model.add(Dense(output_class, activation='softmax'))
    model.summary()
    model.compile(loss='categorical_crossentropy',
                  optimizer='adam',
                  metrics=['accuracy'])
    return model