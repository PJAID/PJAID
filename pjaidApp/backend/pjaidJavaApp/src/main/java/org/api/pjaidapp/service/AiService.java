package org.api.pjaidapp.service;

import org.api.pjaidapp.dto.ai.FaultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class AiService {

    private static final Logger logger = LoggerFactory.getLogger(AiService.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AiService(RestTemplate restTemplate, @Value("${ai.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<FaultDto> getAllFaults() {
        String url = baseUrl + "/predict";
        logger.info("GET {}", url);
        ResponseEntity<FaultDto[]> resp =
                restTemplate.getForEntity(url, FaultDto[].class);
        return Arrays.asList(Objects.requireNonNull(resp.getBody()));
    }

    public List<FaultDto> getTop10Faults() {
        String url = baseUrl + "/predict/top10";
        logger.info("GET {}", url);
        ResponseEntity<FaultDto[]> resp =
                restTemplate.getForEntity(url, FaultDto[].class);
        return Arrays.asList(Objects.requireNonNull(resp.getBody()));
    }
}
