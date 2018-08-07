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

    private static final String CURRENT_VERSION = "current_version";

    private static final String FLAT_AMOUNT = "flat_amount";

    private static final String AMOUNT = "amount";

    private static final String VERSION = "version";

    private static final String CODE = "code";

    private static final String DESCRIPTION = "description";

    @Value("${fee.api.baseUri}")
    private String feeApiUrl;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public Fee getFee(String event) {

        Fee defaultFee = Fee.builder().build();

        log.debug("Getting fee from: ", feeApiUrl);

        URI uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", event)
                .queryParam("jurisdiction1", "family")
                .queryParam("jurisdiction2", "family court")
                .queryParam("service", "divorce")
                .build().toUri();

        ObjectNode[] listOfObjects = restTemplate.getForObject(uri, ObjectNode[].class);

        return Stream.of(listOfObjects).findFirst()
                .map(this::extractValue)
                .orElse(defaultFee);

    }

    private Fee extractValue(ObjectNode objectNode) {
        JsonNode currentVersion = objectNode.path(CURRENT_VERSION);
        double amount = currentVersion
                .path(FLAT_AMOUNT).get(AMOUNT).asDouble();
        int version = currentVersion.path(VERSION).asInt();
        String feeCode = objectNode.path(CODE).asText();
        String description = currentVersion.path(DESCRIPTION).asText();
        return Fee.builder()
                .amount(amount)
                .version(version)
                .description(description)
                .feeCode(feeCode)
                .build();
    }
}