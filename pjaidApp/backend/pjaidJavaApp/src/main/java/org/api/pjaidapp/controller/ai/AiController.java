package org.api.pjaidapp.controller.ai;

import org.api.pjaidapp.dto.ai.FaultDto;
import org.api.pjaidapp.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AiController {

    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/faults")
    public ResponseEntity<List<FaultDto>> getFaults() {
        logger.info("GET /api/faults");
        List<FaultDto> faults = aiService.getAllFaults();
        return new ResponseEntity<>(faults, HttpStatus .OK);
    }

    @GetMapping("/faults/top10")
    public ResponseEntity<List<FaultDto>> getTop10() {
        logger.info("GET /api/faults/top10");
        List<FaultDto> top10 = aiService.getTop10Faults();
        return new ResponseEntity<>(top10, HttpStatus.OK);
    }
}