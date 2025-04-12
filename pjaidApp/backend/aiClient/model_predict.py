#Model logic
import pandas as pd
import chardet
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import classification_report, confusion_matrix
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, Dense
import matplotlib.pyplot as plt
import numpy as np

with open("/Users/adriangoik/PycharmProjects/PJAID/pjaidApp/backend/aiClient/logs/logi_clean.csv", "rb") as f:
    result = chardet.detect(f.read(100000))
    encoding = result["encoding"]
    print("Detected encoding:", encoding)

logi = pd.read_csv("/Users/adriangoik/PycharmProjects/PJAID/pjaidApp/backend/aiClient/logs/logi_clean.csv", encoding=encoding,sep=";",
    on_bad_lines="skip")
drukarki_1 = pd.read_csv("/Users/adriangoik/PycharmProjects/PJAID/pjaidApp/backend/aiClient/logs/drukarki_produkcyjne_clean.csv", encoding=encoding, sep=";",
    on_bad_lines="skip")
drukarki_2 = pd.read_csv("/Users/adriangoik/PycharmProjects/PJAID/pjaidApp/backend/aiClient/logs/drukarki_clean_2.csv", encoding=encoding, sep=";",
    on_bad_lines="skip")




# print ("logi napraw ")
# print(logi.head(), "\n")
#
# print("Drukarki produkcyjne:")
# print(drukarki.head(), "\n")
#
# print("Kalibracje:")
# print(kalibracje.head(), "\n")

drukarki_1.columns=drukarki_1.columns.str.strip()
drukarki_2.columns=drukarki_2.columns.str.strip()
logi.columns=logi.columns.str.strip()

drukarki_all = pd.concat([drukarki_1, drukarki_2], ignore_index=True)

merged = logi.merge(drukarki_all, left_on="ITNumber", right_on="Numer IT", how="left")
print("Połączone dane: ")
print(merged)
print(drukarki_all)
merged.to_csv("logi_połączone.csv", index=False, sep=";")
drukarki_all = drukarki_all.set_index('Numer IT')
naprawy_count = merged.groupby("ITNumber").size().rename("Liczba Napraw")

df = drukarki_all.copy()
df = df.join(naprawy_count, how="left").fillna(0)

numeric_cols = df.select_dtypes(include="number").columns
print("Kolumny numeryczne do modelu:", list(numeric_cols))
df["Łączna liczba napraw"] = df[numeric_cols].sum(axis=1)
print("Podgląd danych wejściowych:")
print(df.head())

for col in df.columns:
    print(f"{col} → {df[col].dtype}")


# Skalowanie danych
X = df[numeric_cols]
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# Trening na "zdrowych drukarkach"
X_train = X_scaled[df["Liczba Napraw"] <= 1]
X_test = X_scaled

# Autoenkoder
input_dim = X_train.shape[1]
input_layer = Input(shape=(input_dim,))
encoded = Dense(8, activation="relu")(input_layer)
decoded = Dense(input_dim, activation="linear")(encoded)
autoencoder = Model(inputs=input_layer, outputs=decoded)
autoencoder.compile(optimizer="adam", loss="mse")

# Trening
autoencoder.fit(X_train, X_train, epochs=50, batch_size=8, verbose=1)

# Rekonstrukcja i MSE
X_pred = autoencoder.predict(X_test)
mse = np.mean(np.square(X_test - X_pred), axis=1)
df["rekonstrukcja_mse"] = mse

# Faktyczne awarie i predykcja
df["Faktyczna_Awaria"] = df["Liczba Napraw"] >= 3
threshold = df["rekonstrukcja_mse"].quantile(0.80)
df["Model_predicted_fault"] = df["rekonstrukcja_mse"] > threshold

# Wyniki
print("Confusion Matrix:")
print(confusion_matrix(df["Faktyczna_Awaria"], df["Model_predicted_fault"]))

print("Raport klasyfikacji:")
print(classification_report(df["Faktyczna_Awaria"], df["Model_predicted_fault"]))



def analyze_model(model):
    plt.figure(figsize=(10, 6))
    plt.scatter(df["Liczba Napraw"], df["rekonstrukcja_mse"], alpha=0.6, c=df["Faktyczna_Awaria"].astype(int),
                cmap="coolwarm")
    plt.xlabel("Liczba napraw")
    plt.ylabel("Błąd rekonstrukcji (MSE)")
    plt.title("Wykrywanie potencjalnych awarii drukarek")
    plt.grid(True)
    plt.colorbar(label="Faktyczna Awaria")
    plt.tight_layout()
    plt.show()

def predict_faults():
    faults = df[df["Model_predicted_fault"] == True][["Liczba Napraw", "rekonstrukcja_mse"]].copy()
    faults["ITNumber"] = faults.index
    faults["Model"] = df.loc[faults.index, "Model"]
    return faults.reset_index()

def get_top_10_faults():
    faults = df[df["Model_predicted_fault"] == True][["Liczba Napraw", "rekonstrukcja_mse"]].copy()
    faults["Numer IT"] = faults.index
    faults["Model"] = df.loc[faults.index, "Model"]
    return faults.sort_values(by="rekonstrukcja_mse", ascending=False).head(10).reset_index(drop=True)

#analiza pyplotem wystarczy wywołanie odcomentować
# analyze_model(autoencoder)
