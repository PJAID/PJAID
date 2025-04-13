package org.api.pjaidapp.controller;

import org.api.pjaidapp.model.IssueReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @PostMapping
    public ResponseEntity<Void> receiveReport(@RequestBody IssueReport report) {
        logger.info("Otrzymano zgłoszenie: Urządzenie={}, Opis='{}'", report.getDeviceId(), report.getDescription());
        return ResponseEntity.ok().build();
    }
}
