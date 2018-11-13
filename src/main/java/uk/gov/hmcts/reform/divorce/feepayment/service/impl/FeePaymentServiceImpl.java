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
import java.util.Objects;

@Service
@Slf4j
public class FeePaymentServiceImpl implements FeePaymentService {

    private static final String AMOUNT = "fee_amount";

    private static final String VERSION = "version";

    private static final String CODE = "code";

    private static final String DESCRIPTION = "description";

    public static final String OTHER = "other";

    public static final String DIVORCE = "divorce";

    public static final String FINANCIAL_ORDER = "financial-order";

    public static final String HIJ = "HIJ";

    @Value("${fee.api.baseUri}")
    private String feeApiUrl;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public Fee getFee(String event, String service, String keyword) {
        URI uri = buildURI(event, service, keyword);
        return getFromRegister(uri);
    }

    @Override
    public Fee getIssueFee() {
        return getFee("issue", DIVORCE, null );
    }

    @Override
    public Fee getAmendPetitionFee() {
        return getFee("issue", OTHER, "ABC" );
    }

    @Override
    public Fee getDefendPetitionFee() {
        return getFee("issue", OTHER, "PQR" );
    }

    @Override
    public Fee getGeneralApplicationFee() {
        return getFee("general application", OTHER, "");
    }

    @Override
    public Fee getEnforcementFee() {
        return getFee("enforcement", OTHER, HIJ);
    }

    @Override
    public Fee getApplicationFinancialOrderFee() {
        return getFee("miscellaneous", OTHER, FINANCIAL_ORDER);
    }

    @Override
    public Fee getApplicationWithoutNoticeFee() {
        return getFee("general application", OTHER, "without-notice" );
    }


    private Fee getFromRegister(URI uri) {
        return extractValue(Objects.requireNonNull(restTemplate.getForObject(uri, ObjectNode.class)));
    }

    private URI buildURI(String event, String divorce, String keyword) {
        URI uri;

        if (keyword == null) {
            uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", event)
                .queryParam("jurisdiction1", "family")
                .queryParam("jurisdiction2", "family court")
                .queryParam("service", divorce)
                .build().toUri();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(feeApiUrl)
                .queryParam("channel", "default")
                .queryParam("event", event)
                .queryParam("jurisdiction1", "family")
                .queryParam("jurisdiction2", "family court")
                .queryParam("service", divorce)
                .queryParam("keyword", keyword)
                .build().toUri();
        }
        return uri;
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