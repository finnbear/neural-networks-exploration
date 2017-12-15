import numpy
import matplotlib.pyplot as plt

def sigmoid(x, derivative=False):
	if derivative == True:
		return sigmoid(x) * (1 - sigmoid(x))

	return 1 / (1 + numpy.exp(-x))

X = []
Y = []
D = []

for x_i in range(-10000, 10000):
	x = float(x_i) / 100

	X.append(x)
	Y.append(sigmoid(x))
	D.append(sigmoid(x, derivative=True))

plt.grid()
plt.scatter(X, Y, s=1)
plt.scatter(X, D, s=1)
plt.title("Sigmoid Function and its Derivative")
plt.xlabel("x")
plt.ylabel("y")
plt.xlim([-10.5, 10.5])
plt.ylim([-0.05, 1.05])
plt.legend(["Sigmoid", "Derivative"])
plt.show()