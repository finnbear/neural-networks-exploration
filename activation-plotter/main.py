import matplotlib.pyplot as plt

plt.grid
plt.scatter(xrange(0, len(history.history['acc']), 10), history.history['loss'][::10], s=2)
plt.title("Sigmoid Function and its Derivative")
plt.xlabel("x")
plt.ylabel("y")
plt.show()