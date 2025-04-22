import sys
import os
from fastapi.testclient import TestClient
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from app import app

client = TestClient(app)

def test_root():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"message": "Ai Service Running and ready!"}

def test_predict():
    response = client.get("/predict")
    assert response.status_code == 200
    assert isinstance(response.json(), list)

def test_predict_top10():
    response = client.get("/predict/top10")
    assert response.status_code == 200
    assert isinstance(response.json(), list)
