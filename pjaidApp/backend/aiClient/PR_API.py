import prority_rate
from fastapi import FastAPI

from fastapi import HTTPException
app=FastAPI()
@app.get("/getPrinters")
def get_printers():
    try:
        printer_list = prority_rate.getAllPrinters()
        return {"printers": printer_list}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/getPrinter/{id}")
def get_printer(id):
    try:
        printer_list = prority_rate.getAllPrinters()
        printer=prority_rate.showPrinterInfo(id)
        return {"printer": printer}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
@app.post("/breakPrinter/{id}")
def break_printer(id):
    try:
        prority_rate.break_device(id)
        printer=prority_rate.showPrinterInfo(id)
        return {"printer": printer}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
@app.get("/calculate")
def calculate():
    try:
        calculations=prority_rate.heuristic_calculation()
        if calculations is None:
            return {"calculations": "everything work"}
        else:
            return {"calculations": calculations}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
@app.post("/fixPrinter/{id}")
def fix_device(id):
    try:
        prority_rate.fix_device(id)
        printer = prority_rate.showPrinterInfo(id)
        return {"printer": printer}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
