from fastapi import FastAPI, HTTPException
from pydantic import BaseModel #for validation and serialization of data in FastAPI
import model #model with future logic

app = FastAPI()

class RequestData(BaseModel):
    task_id: int
    data: dict

@app.get("/")
async def root():
    return {"message": "Ai Service Running and ready!"}
@app.post("/process")
async def process_task(request: RequestData):
    result = model.process_request(request.data)
    return {"task_id":request.task_id, "result":result}

if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app, host='0.0.0.0', port=8000)


