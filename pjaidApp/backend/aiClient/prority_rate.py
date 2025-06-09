import pandas as pd
import os
import numpy as np
import skfuzzy as fuzz
from skfuzzy import control as ctrl
from packaging import version
import networkx as nx
import scipy
import openpyxl
from pydantic import BaseModel

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
name = os.path.abspath(os.path.join(BASE_DIR, "..", "..", "..", "Dane", "drukarki.xlsx"))
class PrinterInput(BaseModel):
    id: str
    IT_number: str
    model: str
    serial_number: str
    place: str
    printed_pages: int
class Printer:
    def __init__(self, id, IT_number, model, serial_number, place, printed_pages, status="OK"):
        self.id = id
        self.IT_number = IT_number
        self.model = model
        self.serial_number = serial_number
        self.place = place
        self.printed_pages = printed_pages
        self.status = status

    def update_pages_number(self, value):
        self.printed_pages += value

    def break_printer(self):
        self.status = "BROKEN"

    def fix_printer(self):
        self.status = "OK"

    def __str__(self):
        return (f" id: {self.id},"
                f" Numer IT: {self.IT_number},"
                f" Model: {self.model},"
                f" Numer seryjny: {self.serial_number},"
                f" miejsce : {self.place},"
                f" wydruki: {self.printed_pages},"
                f" status: {self.status}")
    def to_dict(self):
        return {
            "id": self.id,
            "IT_number": self.IT_number,
            "model": self.model,
            "serial_number": self.serial_number,
            "place": self.place,
            "printed_pages": self.printed_pages,
            "status": self.status
        }



def readFromExel(name):
    data = pd.read_excel(name)
    printers_list = []
    for index, row in data.iterrows():
        printer = Printer(
            id=str(row['Numer']).strip(),
            IT_number=str(row['Numer IT']).strip(),
            model=str(row['Model']).strip(),
            serial_number=str(row['Numer seryjny']).strip(),
            place=str(row['Lokalizacja']).strip(),
            printed_pages=int(row['Total']),
            status=str(row['Status']).strip() if 'Status' in row and not pd.isna(row['Status']) else "OK"
        )
        printers_list.append(printer)
    return printers_list
def saveToExcel(name,printer_list):
    data = []
    for printer in printer_list:
        data.append({
            "Numer": printer.id,
            "Numer IT": printer.IT_number,
            "Model": printer.model,
            "Numer seryjny": printer.serial_number,
            "Lokalizacja": printer.place,
            "Total": printer.printed_pages,
            "Status": printer.status
        })
    df = pd.DataFrame(data)
    df.to_excel(name, index=False)


def search_printer(data, printer_list):
    data = str(data).strip()
    response = []
    for printer in printer_list:
        if (printer.id == data or printer.IT_number == data or printer.model == data or printer.serial_number == data):
            return printer

    if response:
        return response  # zwróć listę drukarek (może mieć 1 lub więcej)
    else:
        return []
def add_pages(data, printer_list):
    pages, id = data.split(",")
    pages = int(pages)
    for printer in printer_list:
        if (printer.id == id):
            printer.update_pages_number(pages)
            print(f"dodano {pages} stron do drukarki nr {id}")
            return
    print("nie znaleziono drukarki")


def break_printer(id, printer_list):
    for printer in printer_list:
        if printer.id == id:
            printer.break_printer()
            saveToExcel(name,printer_list)

def fix_printer(id, printer_list):
    for printer in printer_list:
        if (printer.id == id):
            printer.fix_printer()
            saveToExcel(name,printer_list)

def heuristic_calculation(printer_list):
    priority_value_list=[]
    print_count = ctrl.Antecedent(np.arange(0, 30000000, 100000), "print_count")
    broken_printers = ctrl.Antecedent(np.arange(0, 50, 1), "broken_printers")
    priority = ctrl.Consequent(np.arange(0, 13, 1), "priority")

    # Definicja funkcji przynależności
    print_count['low'] = fuzz.trimf(print_count.universe, [0, 0, 30000])
    print_count['medium'] = fuzz.trimf(print_count.universe, [20000, 60000, 100000])
    print_count['high'] = fuzz.trimf(print_count.universe, [85000, 200000, 30000000])

    broken_printers['low'] = fuzz.trimf(broken_printers.universe, [0, 1, 2])
    broken_printers['medium'] = fuzz.trimf(broken_printers.universe, [1, 3, 4])
    broken_printers['high'] = fuzz.trimf(broken_printers.universe, [3, 5, 50])

    priority['low'] = fuzz.trimf(priority.universe, [0, 0, 4])
    priority['mid'] = fuzz.trimf(priority.universe, [3, 5, 7])
    priority['high'] = fuzz.trimf(priority.universe, [6, 9, 11])
    priority['critical'] = fuzz.trimf(priority.universe, [9, 12, 12])

    # Zdefiniowanie reguł
    rules = []
    rules.append(ctrl.Rule(print_count['high'] & broken_printers['high'], priority['critical']))
    rules.append(ctrl.Rule(print_count['medium'] & broken_printers['high'], priority['high']))
    rules.append(ctrl.Rule(print_count['low'] & broken_printers['high'], priority['mid']))
    rules.append(ctrl.Rule(print_count['high'] & broken_printers['medium'], priority['high']))
    rules.append(ctrl.Rule(print_count['medium'] & broken_printers['medium'], priority['mid']))
    rules.append(ctrl.Rule(print_count['low'] & broken_printers['medium'], priority['mid']))
    rules.append(ctrl.Rule(print_count['high'] & broken_printers['low'], priority['mid']))
    rules.append(ctrl.Rule(print_count['medium'] & broken_printers['low'], priority['mid']))
    rules.append(ctrl.Rule(print_count['low'] & broken_printers['low'], priority['low']))

    # Tworzenie systemu kontrolnego
    priority_ctrl = ctrl.ControlSystem(rules)

    # Obliczanie priorytetu dla każdej drukarki
    for printer in printer_list:
        if printer.status == "BROKEN":
            priority_fuzzy = ctrl.ControlSystemSimulation(priority_ctrl)

            # Wprowadzenie danych do systemu
            priority_fuzzy.input['print_count'] = printer.printed_pages
            priority_fuzzy.input['broken_printers'] = sum(
                1 for p in printer_list if p.place == printer.place and p.status == "BROKEN")

            # Obliczenie
            priority_fuzzy.compute()

            # Sprawdzanie dostępnych kluczy w output
            print(f"Output keys: {priority_fuzzy.output.keys()}")  # Dodanie kontroli dostępnych kluczy

            # Sprawdzanie wyniku obliczeń
            for printer in printer_list:
                if printer.status == "BROKEN":
                    priority_fuzzy = ctrl.ControlSystemSimulation(priority_ctrl)

                    priority_fuzzy.input['print_count'] = printer.printed_pages
                    priority_fuzzy.input['broken_printers'] = sum(
                        1 for p in printer_list if p.place == printer.place and p.status == "BROKEN"
                    )

                    try:
                        priority_fuzzy.compute()
                        if 'priority' in priority_fuzzy.output:
                            priority_value = priority_fuzzy.output['priority']
                            priority_value_list.append({
                                "id": printer.id,
                                "priority": priority_value
                            })
                    except Exception as e:
                        print(f"Błąd przy obliczaniu dla drukarki {printer.id}: {e}")

            return priority_value_list
def getAllPrinters():
    return readFromExel(name)
import os

def addPrinterObj(data: PrinterInput):
    try:
        printer = Printer(
            id=data.id.strip(),
            IT_number=data.IT_number.strip(),
            model=data.model.strip(),
            serial_number=data.serial_number.strip(),
            place=data.place.strip(),
            printed_pages=data.printed_pages,
            status="OK"
        )
        print(f"Próba wczytania pliku: {name}, istnieje? {os.path.exists(name)}")

        df = pd.read_excel(name)
        new_row = {
            'Numer': printer.id,
            'Numer IT': printer.IT_number,
            'Model': printer.model,
            'Numer seryjny': printer.serial_number,
            'Lokalizacja': printer.place,
            'Total': printer.printed_pages,
            'Status': printer.status
        }
        new_row_df = pd.DataFrame([new_row])
        df = pd.concat([df, new_row_df], ignore_index=True)
        df.to_excel(name, index=False)
        print("Dodano nowy wiersz i zapisano plik.")
        return [f"✅ Dodano drukarkę: {printer.id}", printer.id]
    except Exception as e:
        print(f"Błąd podczas dodawania drukarki: {e}")
        return f"❌ Wystąpił błąd: {str(e)}"


def main():
    printer_list = readFromExel(name)
    response = input("podaj czynność: ")
    while True:
        if response == "add":
            data = input("podaj dane drukarki {id,IT_number,model,serial_number,place,printed_pages}")
            addPrinterObj(data, printer_list)
        if response == "info":
            data = input("podaj nr: ")
            search_printer(data, printer_list)
        if response == "Q" or response == "q":
            print("Bye!")
            exit(0)
        if response == "all":
            print(getAllPrinters(printer_list))
        if response == "page":
            data = input("ile stron, id")
            add_pages(data, printer_list)
        if response == "break":
            data = input("id")
            break_printer(data, printer_list)
            saveToExcel(data, printer_list)
        if response == "fix":
            data = input("id")
            fix_printer(data, printer_list)
            saveToExcel(data, printer_list)
        if response == "calculate":
            heuristic_calculation(printer_list)
        response = input("podaj czynność: ")


