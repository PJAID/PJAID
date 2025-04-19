from fastapi import FastAPI, HTTPException
from pydantic import BaseModel #for validation and serialization of data in FastAPI
from starlette.responses import JSONResponse

from model_predict import predict_faults
from model_predict import get_top_10_faults
import uvicorn

app = FastAPI()

class RequestData(BaseModel):
    task_id: int
    data: dict

@app.get("/")
async def root():
    return {"message": "Ai Service Running and ready!"}
@app.post("/process")
async def process_task(request: RequestData):
    try:
        result = model.process_request(request.data)
        return {"task_id":request.task_id, "result":result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
@app.get("/predict")
def get_suspected_devices():
    faults = predict_faults()
    return faults.to_dict(orient="records")
@app.get("/predict/top10")
def top_10_predictions():
    top_10_faults = get_top_10_faults()
    response = []
    for _, row in top_10_faults.iterrows():
        response.append({
            "numerIt": row["Numer IT"],
            "model": row["Model"],
            "rekonstrukcjaMse": row["rekonstrukcja_mse"],
            "liczbaNapraw": int(row["Liczba Napraw"])
        })

    return JSONResponse(content=response)

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host='0.0.0.0', port=8000)


