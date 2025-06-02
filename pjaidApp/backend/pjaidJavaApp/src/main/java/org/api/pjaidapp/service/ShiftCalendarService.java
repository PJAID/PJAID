package org.api.pjaidapp.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.api.pjaidapp.model.Shift;
import org.api.pjaidapp.repository.ShiftRepository;
import org.api.pjaidapp.dto.ShiftDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftCalendarService {

    private static final Logger logger = LoggerFactory.getLogger(ShiftCalendarService.class);

    private final ShiftRepository shiftRepository;
    private final Map<LocalDate, String> schedule = new HashMap<>();

    public void loadFromExcel() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("shift_schedule.xlsx");
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            int count = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Cell dateCell = row.getCell(0);
                Cell shiftCell = row.getCell(1);
                Cell hourFromCell = row.getCell(2);
                Cell hourToCell = row.getCell(3);
                Cell durationCell = row.getCell(4);

                if (dateCell == null || shiftCell == null) continue;

                LocalDate date;
                if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
                    date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                } else {
                    String dateStr = formatter.formatCellValue(dateCell);
                    date = LocalDate.parse(dateStr);
                }

                String shift = formatter.formatCellValue(shiftCell).trim();
                String hourFrom = formatter.formatCellValue(hourFromCell).trim();
                String hourTo = formatter.formatCellValue(hourToCell).trim();
                int duration = (int) durationCell.getNumericCellValue();

                Shift entity = Shift.builder()
                        .date(date)
                        .shift(shift)
                        .hourFrom(hourFrom)
                        .hourTo(hourTo)
                        .duration(duration)
                        .build();

                shiftRepository.save(entity);
                count++;
            }

            logger.info("Harmonogram zmian został załadowany — załadowano {} dat.", count);

        } catch (Exception e) {
            logger.error("Błąd podczas ładowania grafików z Excela: ", e);
        }
    }


    public List<ShiftDto> getShiftsByDate(LocalDate date) {
        logger.info("Szukam zmian dla daty: {}", date);
        List<Shift> shifts = shiftRepository.findAllByDate(date);
        logger.info("Znaleziono {} zmian dla daty {}", shifts.size(), date);

        for (Shift s : shifts) {
            logger.info("-> {} {} {}-{}", s.getShift(), s.getDate(), s.getHourFrom(), s.getHourTo());
        }

        return shifts.stream()
                .map(shift -> new ShiftDto(
                        shift.getShift(),
                        shift.getDate().toString(),
                        shift.getHourFrom() + " - " + shift.getHourTo()
                ))
                .collect(Collectors.toList());
    }


    public String getShiftForDate(LocalDate date) {
        List<Shift> shifts = shiftRepository.findAllByDate(date);
        if (shifts.isEmpty()) {
            return "Brak zmiany";
        }
        return shifts.stream()
                .map(Shift::getShift)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    public List<ShiftDto> getActiveShiftsNow() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        return shiftRepository.findAllByDate(today).stream()
                .filter(shift -> {
                    try {
                        if (shift.getHourFrom().equals("0:00") && shift.getHourTo().equals("0:00")) return false;

                        LocalTime from = LocalTime.parse(shift.getHourFrom(), formatter);
                        LocalTime to = LocalTime.parse(shift.getHourTo(), formatter);

                        // Nocna zmiana (np. 18:00–06:00)
                        if (from.isAfter(to)) {
                            return now.isAfter(from) || now.isBefore(to);
                        } else {
                            return !now.isBefore(from) && !now.isAfter(to);
                        }
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(shift -> new ShiftDto(
                        shift.getShift(),
                        shift.getDate().toString(),
                        shift.getHourFrom() + " - " + shift.getHourTo()
                ))
                .collect(Collectors.toList());
    }



    public List<ShiftDto> getCurrentShifts() {
        return shiftRepository.findAll().stream()
                .map(shift -> new ShiftDto(
                        shift.getShift(),
                        shift.getDate().toString(),
                        shift.getHourFrom() + " - " + shift.getHourTo()
                ))
                .collect(Collectors.toList());
    }



}
