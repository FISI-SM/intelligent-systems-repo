import mlflow
import torch
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import accuracy_score
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import argparse


# Importar la clase Adaline
from adaline import Adaline

# Configurar argumentos
parser = argparse.ArgumentParser()
parser.add_argument("--l_rate", type=float, default=0.01)
parser.add_argument("--iter", type=int, default=100)
args = parser.parse_args()

# Cargar datos
data_available = pd.read_csv("data/data_available.csv")
X = data_available.drop("target", axis=1).values  # Caracteristicas
y = data_available["target"].values  # Etiquetas

# Preprocesamiento
scaler = StandardScaler()
X_standardized = scaler.fit_transform(X)
X_tensor = torch.tensor(X_standardized, dtype=torch.float32)
y_tensor = torch.tensor(y, dtype=torch.long)

# Dividir en entrenamiento y prueba
indices = torch.randperm(X_tensor.size(0))
train_size = int(0.7 * len(X_tensor))
train_indices = indices[:train_size]
test_indices = indices[train_size:]
X_train, X_test = X_tensor[train_indices], X_tensor[test_indices]
y_train, y_test = y_tensor[train_indices], y_tensor[test_indices]


# Iniciar un run de MLflow
with mlflow.start_run():
    # Entrenar modelo
    adaline = Adaline(l_rate=args.l_rate, n_iter=args.iter)
    adaline.fit(X_train, y_train)
    adaline.plot_decision_boundary(X_train, y_train)

    # Hacer predicciones
    y_pred = adaline.predict(X_test)
    y_pred_bolean = torch.round(y_pred)
    accuracy = (y_pred_bolean == y_test).float().mean()
    accuracy = accuracy.item()

    # Registrar par?metros y m?tricas
    mlflow.log_param("l_rate", args.l_rate)
    mlflow.log_param("n_iter", args.iter)
    mlflow.log_metric("accuracy", accuracy)

    # Guardar predicciones
    pd.DataFrame(y_pred_bolean.numpy(), columns=["predicciones"]).to_csv(
        "outputs/predictions.csv", index=False
    )
    mlflow.log_artifact("outputs/predictions.csv")

    # Guardar el grafico como artefacto
    mlflow.log_artifact("outputs/scatter_with_separator.png")

    # Guardar el modelo (serializando weights y bias)
    model_info = {"weights": adaline.weights.numpy(), "bias": adaline.bias.numpy()}
    np.savez("outputs/model.npz", **model_info)
    mlflow.log_artifact("outputs/model.npz")

    print(f"Modelo entrenado con accuracy: {accuracy*100}%")
