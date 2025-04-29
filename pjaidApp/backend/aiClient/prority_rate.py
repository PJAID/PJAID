import pandas as pd
import os
import numpy as np
import skfuzzy as fuzz
from skfuzzy import control as ctrl
from packaging import version
import networkx as nx
import scipy
import openpyxl

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
            printed_pages=int(row['Total'])
        )
        printers_list.append(printer)
    return printers_list


def search_printer(data, printer_list):
    data = str(data).strip()
    response = []
    for printer in printer_list:
        if (printer.id == data or printer.IT_number == data or printer.model == data or printer.serial_number == data):
            response.append(printer)

    if response:
        print("Znalezione drukarki:")
        for printer in response:
            print(printer)
    else:
        print("Nie znaleziono żadnej drukarki dla:", data)


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
        if (printer.id == id):
            printer.break_printer()


def fix_printer(id, printer_list):
    for printer in printer_list:
        if (printer.id == id):
            printer.fix_printer()


def heuristic_calculation(printer_list):
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
            if 'priority' in priority_fuzzy.output:
                priority_value = priority_fuzzy.output['priority']
                print(f"Printer: {printer.id}, Printed Pages: {printer.printed_pages}, "
                      f"Broken Printers in Place: {sum(1 for p in printer_list if p.place == printer.place and p.status == 'BROKEN')}, "
                      f"Priority: {priority_value}")
            else:
                print(f"Printer: {printer.id} - No priority value computed.")

def main():
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    name = os.path.join(BASE_DIR, "..", "..", "..", "Dane", "drukarki.xlsx")
    printer_list = readFromExel(name)
    response = input("podaj czynność: ")
    while True:
        if response == "add":
            data = input("podaj dane drukarki {id,IT_number,model,serial_number,place,printed_pages}")
            try:
                id, IT_number, model, serial_number, place, printed_pages = data.split(",")
                printer = Printer(
                    id=id.strip(),
                    IT_number=IT_number.strip(),
                    model=model.strip(),
                    serial_number=serial_number.strip(),
                    place=place.strip(),
                    printed_pages=int(printed_pages.strip()),
                    status="OK"
                )
                printer_list.append(printer)
                print("✅ Dodano drukarkę:", printer)
            except ValueError:
                print("❌ Błąd: podano złą liczbę parametrów. Spróbuj ponownie.")
        if response == "info":
            data = input("podaj nr: ")
            search_printer(data, printer_list)
        if response == "Q" or response == "q":
            print("Bye!")
            exit(0)
        if response == "all":
            for printer in printer_list:
                print(printer)
        if response == "page":
            data = input("ile stron, id")
            add_pages(data, printer_list)
        if response == "break":
            data = input("id")
            break_printer(data, printer_list)
        if response == "fix":
            data = input("id")
            fix_printer(data, printer_list)
        if response == "calculate":
            heuristic_calculation(printer_list)
        response = input("podaj czynność: ")

main()
