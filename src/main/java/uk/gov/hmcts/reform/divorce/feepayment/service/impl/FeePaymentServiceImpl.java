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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FeePaymentServiceImpl implements FeePaymentService {

    private static final String AMOUNT = "fee_amount";
    private static final String VERSION = "version";
    private static final String CODE = "code";
    private static final String DESCRIPTION = "description";
    private static final String OTHER = "other";
    private static final String DIVORCE = "divorce";
    private static final String FINANCIAL_ORDER = "financial-order";
    private static final String HIJ = "HIJ";
    private static final String ISSUE = "issue";
    private static final String GENERAL_APPLICATION = "general application";

    @Value("${fee.api.keyword}")
    private String feeKeyword;
    private final String feesLookupEndpoint;
    private final RestTemplate restTemplate;
    private String feeApiBaseUri;
    private final String[][] feesItems = {
        {ISSUE, DIVORCE, null},
        {ISSUE, OTHER, "ABC"},
        {GENERAL_APPLICATION, OTHER, null},
        {"enforcement", OTHER, HIJ},
        {"miscellaneous", OTHER, FINANCIAL_ORDER},
        {GENERAL_APPLICATION, OTHER, feeKeyword},
        {ISSUE, OTHER, "PQR"}
    };

    @Autowired
    public FeePaymentServiceImpl(RestTemplate restTemplate,
        @Value("${fee.api.baseUri}") String feeApiBaseUri,
        @Value("${fee.api.feesLookup}") String feesLookupEndpoint) {
        this.restTemplate = restTemplate;
        this.feeApiBaseUri = feeApiBaseUri;
        this.feesLookupEndpoint = feesLookupEndpoint;
    }

    @Override
    public Fee getFee(String event, String service, String keyword) {
        URI uri = buildURI(event, service, keyword);
        return getFromRegister(uri);
    }

    @Override
    public Fee getIssueFee() {
        return getFee(ISSUE, DIVORCE, null );
    }

    @Override
    public Fee getAmendPetitionFee() {
        return getFee(ISSUE, OTHER, "ABC" );
    }

    @Override
    public Fee getDefendPetitionFee() {
        return getFee(ISSUE, OTHER, "PQR" );
    }

    @Override
    public Fee getGeneralApplicationFee() {
        return getFee(GENERAL_APPLICATION, OTHER, null);
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
        return getFee(GENERAL_APPLICATION, OTHER, feeKeyword);
    }

    private Fee getFromRegister(URI uri) {
        return extractValue(Objects.requireNonNull(restTemplate.getForObject(uri, ObjectNode.class)));
    }

    private URI buildURI(String event, String divorce, String keyword) {
        URI uri;

        if (keyword == null) {
            uri = UriComponentsBuilder.fromHttpUrl(feeApiBaseUri + feesLookupEndpoint)
                .queryParam("channel", "default")
                .queryParam("event", event)
                .queryParam("jurisdiction1", "family")
                .queryParam("jurisdiction2", "family court")
                .queryParam("service", divorce)
                .build().toUri();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(feeApiBaseUri + feesLookupEndpoint)
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

    @Override
    public List<Fee> getAllFees() {

        return Stream.of(feesItems).map(i -> getFee(i[0], i[1], i[2])).collect(Collectors.toList());
    }
}
