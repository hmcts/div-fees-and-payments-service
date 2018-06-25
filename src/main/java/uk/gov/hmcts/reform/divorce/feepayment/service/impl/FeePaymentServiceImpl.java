package uk.gov.hmcts.reform.divorce.feepayment.service.impl;

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
        double amount = objectNode.path("current_version")
                .path("flat_amount").get("amount").asDouble();
        int version = objectNode.path("current_version").path("version").asInt();
        String feeCode = objectNode.path("code").asText();
        return Fee.builder().amount(amount).version(version).feeCode(feeCode).build();
    }


    public void setFeeApiUrl(String feeApiUrl) {
        this.feeApiUrl = feeApiUrl;
    }
}