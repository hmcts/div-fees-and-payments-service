package uk.gov.hmcts.reform.divorce.feepayment.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

import java.net.URI;
import java.util.stream.Stream;

@Service
@Slf4j
public class FeePaymentServiceImpl implements FeePaymentService {

    private static final String AMOUNT = "fee_amount";

    private static final String VERSION = "version";

    private static final String CODE = "code";

    private static final String DESCRIPTION = "description";

    @Value("${fee.api.baseUri}")
    private String feeApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Fee getFee(String event) {

        log.debug("Getting fee from: " + feeApiUrl);

        URI uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", event)
                .queryParam("jurisdiction1", "family")
                .queryParam("jurisdiction2", "family court")
                .queryParam("service", "divorce")
                .build().toUri();

        ObjectNode feeResponse = restTemplate.getForObject(uri, ObjectNode.class);

        return extractValue(feeResponse);
    }

    private Fee extractValue(ObjectNode objectNode) {
        double amount = objectNode.path(AMOUNT).asDouble();
        int version = objectNode.path(VERSION).asInt();
        String feeCode = objectNode.path(CODE).asText();
        String description = objectNode.path(DESCRIPTION).asText();
        
        return Fee.builder()
                .amount(amount)
                .version(version)
                .description(description)
                .feeCode(feeCode)
                .build();
    }
}