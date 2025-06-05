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
        printer=prority_rate.search_printer(id, printer_list)
        return {"printer": printer}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
@app.post("/breakPrinter/{id}")
def break_printer(id):
    try:
        printer_list = prority_rate.getAllPrinters()
        prority_rate.break_printer(id, printer_list)
        printer=prority_rate.search_printer(id, printer_list)
        return {"printer": printer}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
@app.get("/calculate")
def calculate():
    try:
        printer_list = prority_rate.getAllPrinters()
        calculations=prority_rate.heuristic_calculation(printer_list)
        if calculations is None:
            return {"calculations": "everything work"}
        else:
            return {"calculations": calculations}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
@app.post("/fixPrinter/{id}")
def break_printer(id):
    try:
        printer_list = prority_rate.getAllPrinters()
        prority_rate.fix_printer(id, printer_list)
        printer=prority_rate.search_printer(id, printer_list)
        return {"printer": printer}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
