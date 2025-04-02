import mlflow
import torch
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


# Definir la clase Adaline
class Adaline:
    def __init__(self, l_rate=0.01, n_iter=100):
        self.l_rate = l_rate
        self.n_iter = n_iter
        self.weights = None
        self.bias = None

    def fit(self, X, y):
        self.weights = torch.randn(X.shape[1], dtype=torch.float32) * 0.01
        self.bias = torch.zeros(1, dtype=torch.float32)
        for i in range(self.n_iter):
            y_pred = self.predict(X)
            error = y.float() - y_pred  # Convertir y a float para c?lculos
            self.weights += self.l_rate * torch.matmul(X.T, error)
            self.bias += self.l_rate * error.sum()

    def predict(self, X):
        return torch.matmul(X, self.weights) + self.bias

    def plot_decision_boundary(self, X, y):
        # Visualizaci?n
        X_np = X.numpy()
        y_np = y.numpy()

        # Crear el gr?fico de dispersi?n
        plt.figure(figsize=(6, 6))
        plt.scatter(X_np[y_np == 0, 0], X_np[y_np == 0, 1], color="red", label="Setosa")
        plt.scatter(
            X_np[y_np == 1, 0], X_np[y_np == 1, 1], color="blue", label="Versicolor"
        )

        # Obtener los par?metros del modelo (pesos y sesgo)
        w0 = self.bias.item()  # Sesgo (intercepto)
        w1, w2 = (
            self.weights[0].item(),
            self.weights[1].item(),
        )  # Pesos de las caracter?sticas

        # Graficar la l?nea separadora de ADALINE
        x1_vals = np.linspace(X_np[:, 0].min(), X_np[:, 0].max(), 100)
        x2_vals = (-w0 - w1 * x1_vals) / w2  # Ecuaci?n de la l?nea separadora

        # Graficar la l?nea separadora
        plt.plot(
            x1_vals,
            x2_vals,
            color="green",
            label="Linea Separadora (ADALINE)",
            linestyle="--",
        )

        # Configurar el gr?fico
        plt.xlabel("Sepal Length")
        plt.ylabel("Petal Length")
        plt.title("Distribucion de Iris Setosa y Versicolor con Linea Separadora")
        plt.legend()
        plt.savefig("outputs/scatter_with_separator.png")  # Guardar como artefacto
        plt.close()
