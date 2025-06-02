import os
import re
import json
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
from sklearn.preprocessing import StandardScaler
from tensorflow.keras.models import Model, load_model
from tensorflow.keras.layers import Input, Dense
from keras.losses import MeanSquaredError

CSV_PATH = "logs/logs_clean.csv"
MODEL_PATH = "autoencoder_model.h5"
JSON_OUTPUT_PATH = "predykcje_awarii.json"
RETRAIN_AFTER_DAYS = 7

# Wczytanie danych
df = pd.read_csv(CSV_PATH)
df.columns = df.columns.str.strip()
df['Data Naprawy'] = pd.to_datetime(df['Data Naprawy'])
df['month'] = df['Data Naprawy'].dt.to_period('M')
df['Liczba Napraw'] = df.groupby('ID_Urządzenia')['ID_Urządzenia'].transform('count')

# Grupowanie do analizy
summary = df.groupby('ID_Urządzenia').agg({'Liczba Napraw': 'max'})
repair_counts = pd.get_dummies(df[['ID_Urządzenia', 'Typ Naprawy']], columns=['Typ Naprawy']) \
    .groupby('ID_Urządzenia').sum()
model_df = summary.join(repair_counts, how='left').fillna(0)

# Przygotowanie danych numerycznych
numeric_cols = model_df.select_dtypes(include='number').columns
X = model_df[numeric_cols]
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)
X_train = X_scaled[model_df['Liczba Napraw'] <= 1]

# Trening AutoEncodera jeśli wymagane
if not os.path.exists(MODEL_PATH) or \
        (datetime.now() - datetime.fromtimestamp(os.path.getmtime(MODEL_PATH))).days >= RETRAIN_AFTER_DAYS:

    input_dim = X_train.shape[1]
    input_layer = Input(shape=(input_dim,))
    encoded = Dense(8, activation="relu")(input_layer)
    decoded = Dense(input_dim, activation="linear")(encoded)
    autoencoder = Model(inputs=input_layer, outputs=decoded)
    autoencoder.compile(optimizer="adam", loss="mse")
    autoencoder.fit(X_train, X_train, epochs=30, batch_size=8, verbose=1)
    autoencoder.save(MODEL_PATH)
else:
    autoencoder = load_model(MODEL_PATH, compile=False)
    autoencoder.compile(optimizer="adam", loss=MeanSquaredError())

# Predykcja i wykrycie outlierów
X_pred = autoencoder.predict(X_scaled)
mse = np.mean(np.square(X_scaled - X_pred), axis=1)
model_df["rekonstrukcja_mse"] = mse
threshold = model_df["rekonstrukcja_mse"].quantile(0.60)
model_df["Anomalia"] = model_df["rekonstrukcja_mse"] > threshold

# Tworzenie JSON-ów tylko jeśli warto
result_jsons = []

for device in model_df[model_df["Anomalia"]].index:
    device_logs = df[df['ID_Urządzenia'] == device].sort_values("Data Naprawy")

    if len(device_logs) < 2:
        continue

    # Obliczamy częstotliwość
    monthly_counts = device_logs.groupby(device_logs['Data Naprawy'].dt.to_period("M")).size()
    avg_monthly_failures = monthly_counts.mean()

    # Średni odstęp
    deltas = device_logs['Data Naprawy'].diff().dropna().dt.days
    if deltas.empty:
        continue
    avg_delta = int(deltas.mean())
    next_date = device_logs['Data Naprawy'].max() + timedelta(days=avg_delta)

    # Warunki generowania ticketów
    if (avg_monthly_failures >= 4 and avg_delta <= 7) or \
            (avg_monthly_failures >= 3 and avg_delta <= 10) or \
            (avg_monthly_failures >= 2 and avg_delta <= 14):
        last_entry = device_logs.iloc[-1]
        raw_text = str(last_entry["Opis Naprawy"]).strip()

        # Podział tylko po średnikach, enterach, bulletach
        description_points = re.split(r"[;\n•●]\s*", raw_text)

        # Łączymy krótkie zdania typu: "To ją przetarłem" z poprzednim
        merged_points = []
        for point in description_points:
            point = point.strip()
            if not point:
                continue
            if len(point) < 20 and merged_points:
                merged_points[-1] += ". " + point[0].lower() + point[1:]
            else:
                merged_points.append(point.capitalize())

        # Usunięcie duplikatów, zachowanie kolejności
        merged_points = list(dict.fromkeys(merged_points))

        # Finalny opis
        full_description = "\n- " + "\n- ".join(merged_points)
        full_description += f"\n- Przewidywana kolejna awaria: {next_date.date()}"

        # rozdziela też po przecinkach, myślnikach, bulletach

        result_jsons.append({
            "ticket_type": "PJAID",
            "device_number": device,
            "repair_type": last_entry["Typ Naprawy"],
            "description": full_description,
            "next_failure_date": next_date.strftime("%Y-%m-%d")
        })

# Zapis do pliku JSON (albo zwróć do API)
with open(JSON_OUTPUT_PATH, "w", encoding="utf-8") as f:
    json.dump(result_jsons, f, ensure_ascii=False, indent=2)

print(f"Zapisano {len(result_jsons)} zgłoszeń do {JSON_OUTPUT_PATH}")
