# Rewritten using KERAS for maximum simplicity
import csv
import numpy
from keras.models import Sequential
from keras.layers import Dense
import matplotlib.pyplot as plt

X = []
Y = []

with open('capacitor-data.csv', 'rb') as file:
	reader = csv.reader(file)

	for row in reader:
		X.append([float(row[0]) / 1000, float(row[1]) / 5])
		Y.append(float(row[2]) / 1000)

x = numpy.asarray(X)
y = numpy.asarray(Y)

print x

model = Sequential()
model.add(Dense(2, activation='tanh', input_dim=2))
model.add(Dense(5, activation='tanh'))
model.add(Dense(5, activation='tanh'))
model.add(Dense(5, activation='tanh'))
model.add(Dense(1, activation='linear'))

model.compile(optimizer='rmsprop', loss='mean_absolute_error', metrics=['accuracy'])

history = model.fit(x=X, y=Y, epochs=5000, batch_size=100)

def predict(X1, X2):
	return model.predict(numpy.asarray([[float(X1) / 1000, float(X2) / 5]]))[0][0]

for i in range(len(X)):
	print(int(model.predict(numpy.asarray([X[i]]))[0][0] * 1000))
print history.history

plt.scatter(xrange(0, len(history.history['acc']), 10), history.history['loss'][::10], s=2)
plt.title("Mean error vs. epoch")
plt.xlabel("Epoch")
plt.ylabel("Mean error")
plt.show()