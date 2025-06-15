import pandas as pd
import numpy as np
import skfuzzy as fuzz
from pip._internal.network import session
from skfuzzy import control as ctrl
from packaging import version
import networkx as nx
import scipy
import openpyxl
from pydantic import BaseModel
import mysql.connector
#baza danych na jakiej testowałem:
# mysql> SELECT * FROM devices;
# +----+----------+---------------+---------------+--------------+--------+-----------+-------+
# | id | name     | serial_number | purchase_date | last_service | status | usibility | place |
# +----+----------+---------------+---------------+--------------+--------+-----------+-------+
# |  1 | Device A | SN001         | 2023-01-01    | 2024-01-01   | BROKEN |         0 | b1    |
# |  2 | Device B | SN002         | 2022-06-15    | 2024-05-15   | BROKEN |     25000 | b1    |
# |  3 | Device C | SN003         | 2021-03-10    | 2024-03-10   | BROKEN |   2000000 | B2    |
# |  4 | Device D | SN004         | 2020-11-20    | 2024-04-01   | OK     |        90 | B2    |
# |  5 | Device E | SN005         | 2023-07-05    | 2024-06-01   | OK     |        85 | B2    |
# |  6 | Device F | SN006         | 2021-09-30    | 2023-09-30   | OK     |        75 | B2    |
# |  7 | Device G | SN007         | 2022-12-12    | 2024-05-01   | OK     |        65 | B2    |
# |  8 | Device H | SN008         | 2020-05-25    | 2023-11-15   | OK     |        95 | B2    |
# |  9 | Device I | SN009         | 2023-03-03    | 2024-03-03   | OK     |        55 | B2    |
# | 10 | Device J | SN010         | 2021-08-08    | 2024-02-28   | OK     |  29000000 | b1    |
# +----+----------+---------------+---------------+--------------+--------+-----------+-------+
class DeviceInput(BaseModel):
    id: str
    IT_number: str
    model: str
    serial_number: str
    place: str
    usubility: int
class Device:
    def __init__(self, id, IT_number, model, serial_number, place, usibility, status="OK"):
        self.id = id
        self.IT_number = IT_number
        self.model = model
        self.serial_number = serial_number
        self.place = place
        self.usibility = usibility
        self.status = status

    def usibility_number(self, value):
        self.usibility += value

    def break_device(self):
        self.status = "BROKEN"

    def fix_device(self):
        self.status = "OK"

    def __str__(self):
        return (f" id: {self.id},"
                f" Numer IT: {self.IT_number},"
                f" Model: {self.model},"
                f" Numer seryjny: {self.serial_number},"
                f" miejsce : {self.place},"
                f" usibility: {self.usibility},"
                f" status: {self.status}")
    def to_dict(self):
        return {
            "id": self.id,
            "IT_number": self.IT_number,
            "model": self.model,
            "serial_number": self.serial_number,
            "place": self.place,
            "usibility": self.usibility,
            "status": self.status
        }



def readFromExel():
    try:
        connection = f_connection()
        if connection.is_connected():
            cursor = connection.cursor(dictionary=True)
            cursor.execute("SELECT * FROM devices")
            device_list = []
            for row in cursor.fetchall():
                device = Device(
                    id=row["id"],
                    IT_number="",
                    model="",
                    serial_number=row["serial_number"],
                    place="",
                    usibility=row["usibility"] or 50,
                    status=row["status"] or "OK"
                )
                device_list.append(device)
            return device_list
    except Exception as e:
        print(f"❌ Błąd podczas czytania z bazy danych: {e}")
        return []
def saveToExcel(name,device_list):
    data = []
    for device in device_list:
        data.append({
            "Numer": device.id,
            "Numer IT": device.IT_number,
            "Model": device.model,
            "Numer seryjny": device.serial_number,
            "Lokalizacja": device.place,
            "Total": device.usibility,
            "Status": device.status
        })
    df = pd.DataFrame(data)
    df.to_excel(name, index=False)
def showPrinterInfo(printer_id):
    try:
        connection = f_connection()
        if connection.is_connected():
            cursor = connection.cursor(dictionary=True)
            cursor.execute("SELECT * FROM devices WHERE id = %s", (printer_id,))
            row = cursor.fetchone()
            if row:
                return {
                    "id": row["id"],
                    "serial_number": row["serial_number"],
                    "usability": row["usibility"],
                    "status": row["status"]
                }
            else:
                print("Nie znaleziono urządzenia o tym ID.")
        connection.close()
    except Exception as e:
        print(f"❌ Błąd podczas pobierania informacji: {e}")
def add_pages(data, printer_list):
    pages, id = data.split(",")
    pages = int(pages)
    for printer in printer_list:
        if (printer.id == id):
            printer.update_pages_number(pages)
            print(f"dodano {pages} stron do drukarki nr {id}")
            return
    print("nie znaleziono drukarki")
def break_device(device_id):
    try:
        connection = f_connection()
        if connection.is_connected():
            cursor = connection.cursor(dictionary=True)

            # Sprawdzenie czy urządzenie istnieje
            cursor.execute("SELECT * FROM devices WHERE id = %s", (device_id,))
            row = cursor.fetchone()

            if row:
                # Aktualizacja statusu
                sql = "UPDATE devices SET status = %s WHERE id = %s"
                cursor.execute(sql, ("BROKEN", device_id))
                connection.commit()
                cursor.close()
                connection.close()
                return f"Urządzenie o ID {device_id} zostało oznaczone jako BROKEN."
            else:
                cursor.close()
                connection.close()
                return "Nie znaleziono urządzenia o tym ID."
        #
        #     cursor.close()
        # connection.close()
    except Exception as e:
        print(f"❌ Błąd podczas operacji na bazie: {e}")
def fix_device(device_id):
    try:
        connection = f_connection()
        if connection.is_connected():
            cursor = connection.cursor(dictionary=True)

            # Sprawdzenie czy urządzenie istnieje
            cursor.execute("SELECT * FROM devices WHERE id = %s", (device_id,))
            row = cursor.fetchone()

            if row:
                # Aktualizacja statusu
                sql = "UPDATE devices SET status = %s WHERE id = %s"
                cursor.execute(sql, ("OK", device_id))
                connection.commit()
                cursor.close()
                connection.close()
                return f"Urządzenie o ID {device_id} zostało oznaczone jako OK."
            else:
                cursor.close()
                connection.close()
                return "Nie znaleziono urządzenia o tym ID."


    except Exception as e:
        print(f"❌ Błąd podczas operacji na bazie: {e}")
def f_connection():
    connection = mysql.connector.connect(
        host="localhost",
        user="root",
        password="mysql",
        database="pjaid"
    )
    return connection
def heuristic_calculation():
    try:
        connection=f_connection()
        cursor = connection.cursor(dictionary=True)
        cursor.execute("SELECT * FROM devices")
        devices = cursor.fetchall()
        connection.close()
    except Exception as e:
        print(f"Błąd przy łączeniu z bazą: {e}")
        return []

    priority_value_list = []

    usibility = ctrl.Antecedent(np.arange(0, 30000000, 100000), "usibility")
    priority = ctrl.Consequent(np.arange(0, 13, 1), "priority")

    # Funkcje przynależności
    usibility['low'] = fuzz.trimf(usibility.universe, [0, 0, 30000])
    usibility['medium'] = fuzz.trimf(usibility.universe, [20000, 60000, 100000])
    usibility['high'] = fuzz.trimf(usibility.universe, [85000, 200000, 30000000])

    priority['low'] = fuzz.trimf(priority.universe, [0, 0, 4])
    priority['mid'] = fuzz.trimf(priority.universe, [3, 5, 7])
    priority['high'] = fuzz.trimf(priority.universe, [6, 9, 11])
    priority['critical'] = fuzz.trimf(priority.universe, [9, 12, 12])

    rules = [
        ctrl.Rule(usibility['high'], priority['critical']),
        ctrl.Rule(usibility['medium'], priority['high']),
        ctrl.Rule(usibility['low'], priority['mid']),
        ctrl.Rule(usibility['high'], priority['high']),
        ctrl.Rule(usibility['medium'], priority['mid']),
        ctrl.Rule(usibility['low'], priority['mid']),
        ctrl.Rule(usibility['high'], priority['mid']),
        ctrl.Rule(usibility['medium'], priority['mid']),
        ctrl.Rule(usibility['low'], priority['low']),
    ]

    priority_ctrl = ctrl.ControlSystem(rules)

    broken_by_place = {}
    for d in devices:
        if d['status'] == "BROKEN":
            place = d['place']
            broken_by_place[place] = broken_by_place.get(place, 0) + 1

    for device in devices:
        if device['status'] == "BROKEN":
            priority_fuzzy = ctrl.ControlSystemSimulation(priority_ctrl)

            usibility_value = device.get('printed_pages', device.get('usibility', 0))
            priority_fuzzy.input['usibility'] = usibility_value

            try:
                priority_fuzzy.compute()
                base_priority = priority_fuzzy.output['priority']

                same_place_broken = broken_by_place.get(device['place'], 0)
                if same_place_broken > 1:
                    adjusted_priority = base_priority + (same_place_broken - 1) * 0.5
                    adjusted_priority = min(adjusted_priority, 12)  # limit max
                else:
                    adjusted_priority = base_priority

                priority_value_list.append({
                    "id": device['id'],
                    "priority": round(float(adjusted_priority), 2)
                })
            except Exception as e:
                return f"Błąd przy obliczaniu dla urządzenia {device['id']}: {e}"

    return priority_value_list


def getAllPrinters():
    device_list=[]
    for device in readFromExel():
        device_list.append(device)
    return device_list
def main():
    device_list = readFromExel()
    response = input("podaj czynność: ")
    while True:
        if response == "info":
            printer_id = int(input("podaj nr: "))
            showPrinterInfo(printer_id)
        if response == "Q" or response == "q":
            print("Bye!")
            exit(0)
        if response == "all":
            for device in getAllPrinters():
                print(device)
        if response == "page":
            data = input("ile stron, id")
            add_pages(data, device_list)
        if response == "break":
            data = input("id")
            break_device(data)
        if response == "fix":
            data = input("id")
            fix_device(data)
            saveToExcel(data, device_list)
        if response == "calculate":
            response=heuristic_calculation()
            for item in response:
                print(f"id: {item['id']} priority: {item['priority']}")
        response = input("podaj czynność: ")


