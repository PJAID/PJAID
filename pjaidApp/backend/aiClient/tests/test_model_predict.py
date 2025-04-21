import sys
import os
import pytest
import pandas as pd

# Dodaj katalog backend/aiClient do sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import model_predict  # <-- prosty import model_predict.py

@pytest.fixture
def sample_df():
    return pd.DataFrame({
        "Liczba Napraw": [0, 5, 0, 10],
        "rekonstrukcja_mse": [0.01, 0.5, 0.02, 0.7],
        "Model_predicted_fault": [False, True, False, True],
        "Model": ["ModelA", "ModelB", "ModelC", "ModelD"],
    }, index=["IT0001", "IT0002", "IT0003", "IT0004"])

def test_predict_faults(sample_df, monkeypatch):
    monkeypatch.setattr(model_predict, "df", sample_df)
    faults = model_predict.predict_faults()
    assert not faults.empty

def test_get_top_10_faults(sample_df, monkeypatch):
    monkeypatch.setattr(model_predict, "df", sample_df)
    top10 = model_predict.get_top_10_faults()
    assert len(top10) <= 10

#model zwraca pola
