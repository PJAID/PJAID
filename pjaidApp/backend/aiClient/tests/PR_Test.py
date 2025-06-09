import unittest
from unittest.mock import MagicMock, patch
from pjaidApp.backend.aiClient.prority_rate import (
    Printer, readFromExel, PrinterInput, addPrinterObj, search_printer,
    add_pages, break_printer, fix_printer, heuristic_calculation
)


class TestPrinterMethods(unittest.TestCase):

    def test_status_changes_and_page_update(self):
        printer = Printer("1", "IT123", "HP LaserJet", "SN123", "Biuro", 100)
        self.assertEqual(printer.status, "OK")

        printer.break_printer()
        self.assertEqual(printer.status, "BROKEN")

        printer.fix_printer()
        self.assertEqual(printer.status, "OK")

        printer.update_pages_number(50)
        self.assertEqual(printer.printed_pages, 150)

    def test_to_dict(self):
        printer = Printer("1", "IT123", "HP", "SN", "Biuro", 123)
        printer_dict = printer.to_dict()
        self.assertEqual(printer_dict["id"], "1")
        self.assertEqual(printer_dict["printed_pages"], 123)


class TestExcelIntegration(unittest.TestCase):
    @patch("pjaidApp.backend.aiClient.prority_rate.readFromExel")
    def test_search_printer(self, mock_read):
        printer = Printer("1", "IT123", "HP", "SN123", "Biuro", 100)
        mock_read.return_value = [printer]
        result = search_printer("1")
        self.assertEqual(result.id, "1")

    @patch("pjaidApp.backend.aiClient.prority_rate.readFromExel")
    def test_add_pages(self, mock_read):
        printer = Printer("1", "IT123", "HP", "SN123", "Biuro", 100)
        mock_read.return_value = [printer]
        add_pages("50,1")
        self.assertEqual(printer.printed_pages, 150)

    @patch("pjaidApp.backend.aiClient.prority_rate.readFromExel")
    @patch("pjaidApp.backend.aiClient.prority_rate.saveToExcel")
    def test_break_printer(self, mock_save, mock_read):
        printer = Printer("1", "IT123", "HP", "SN123", "Biuro", 100)
        mock_read.return_value = [printer]
        break_printer("1")
        self.assertEqual(printer.status, "BROKEN")
        mock_save.assert_called_once()

    @patch("pjaidApp.backend.aiClient.prority_rate.readFromExel")
    @patch("pjaidApp.backend.aiClient.prority_rate.saveToExcel")
    def test_fix_printer(self, mock_save, mock_read):
        printer = Printer("1", "IT123", "HP", "SN123", "Biuro", 100, status="BROKEN")
        mock_read.return_value = [printer]
        fix_printer("1")
        self.assertEqual(printer.status, "OK")
        mock_save.assert_called_once()

    @patch("pjaidApp.backend.aiClient.prority_rate.readFromExel")
    def test_heuristic_calculation(self, mock_read):
        broken = Printer("1", "IT123", "HP", "SN123", "Biuro", 200000, status="BROKEN")
        ok = Printer("2", "IT124", "Canon", "SN124", "Biuro", 10000, status="OK")
        mock_read.return_value = [broken, ok]

        result = heuristic_calculation()
        self.assertIsInstance(result, list)
        self.assertEqual(result[0]["id"], "1")
        self.assertGreaterEqual(result[0]["priority"], 0)
        self.assertLessEqual(result[0]["priority"], 12)


if __name__ == "__main__":
    unittest.main()
