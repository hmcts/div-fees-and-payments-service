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

    private static final String SERVICE_OTHER = "other";
    private static final String SERVICE_DIVORCE = "divorce";

    private static final String EVENT_ISSUE = "issue";
    private static final String EVENT_GENERAL_APPLICATION = "general application";
    private static final String EVENT_ENFORCEMENT = "enforcement";
    private static final String EVENT_MISCELLANEOUS = "miscellaneous";

    private static final String KEYWORD_DIVORCE_APPLICATION = "DivorceCivPart";
    private static final String KEYWORD_AMEND_PETITION = "DivorceAmendPetition";
    private static final String KEYWORD_ORIGINATE_PROCEEDINGS = "AppnPrivateOther";
    private static final String KEYWORD_DECREE_NISI = "GAContestedOrder";
    private static final String KEYWORD_BAILIFF_SERVICE = "BailiffServeDoc";
    private static final String KEYWORD_FINANCIAL_ORDER = "FinancialOrderOnNotice";
    private static final String KEYWORD_WITHOUT_NOTICE = "GeneralAppWithoutNotice";
    protected static final String KEYWORD_ORIGINAL_AMEND = "ABC";
    protected static final String KEYWORD_ORIGINAL_DEFEND = "PQR";
    protected static final String KEYWORD_ORIGINAL_BAILIFF = "HIJ";
    protected static final String KEYWORD_ORIGINAL_FO = "financial-order";

    private final String feesLookupEndpoint;
    private boolean feesPayKeywords;
    private final RestTemplate restTemplate;
    private final String feeApiBaseUri;


    @Autowired
    public FeePaymentServiceImpl(RestTemplate restTemplate,
        @Value("${fee.api.baseUri}") String feeApiBaseUri,
        @Value("${fee.api.feesLookup}") String feesLookupEndpoint,
        @Value("${feature-toggle.toggle.fee-pay-keywords}") Boolean feesPayKeywords) {
        this.restTemplate = restTemplate;
        this.feeApiBaseUri = feeApiBaseUri;
        this.feesLookupEndpoint = feesLookupEndpoint;
        this.feesPayKeywords = feesPayKeywords != null ? feesPayKeywords : false;
    }

    @Override
    public Fee getFee(String event, String service, String keyword) {
        URI uri = buildURI(event, service, keyword);
        return getFromRegister(uri);
    }

    @Override
    public Fee getIssueFee() {
        return getFee(EVENT_ISSUE, SERVICE_DIVORCE, (feesPayKeywords ? KEYWORD_DIVORCE_APPLICATION : null));
    }

    @Override
    public Fee getAmendPetitionFee() {
        return getFee(EVENT_ISSUE, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_AMEND_PETITION : KEYWORD_ORIGINAL_AMEND));
    }

    @Override
    public Fee getDefendPetitionFee() {
        return getFee(EVENT_ISSUE, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_ORIGINATE_PROCEEDINGS : KEYWORD_ORIGINAL_DEFEND));
    }

    @Override
    public Fee getGeneralApplicationFee() {
        return getFee(EVENT_GENERAL_APPLICATION, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_DECREE_NISI : null));
    }

    @Override
    public Fee getEnforcementFee() {
        return getFee(EVENT_ENFORCEMENT, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_BAILIFF_SERVICE : KEYWORD_ORIGINAL_BAILIFF));
    }

    @Override
    public Fee getApplicationFinancialOrderFee() {
        return getFee(EVENT_MISCELLANEOUS, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_FINANCIAL_ORDER : KEYWORD_ORIGINAL_FO));
    }

    @Override
    public Fee getApplicationWithoutNoticeFee() {
        return getFee(EVENT_GENERAL_APPLICATION, SERVICE_OTHER, KEYWORD_WITHOUT_NOTICE);
    }

    private Fee getFromRegister(URI uri) {
        return extractValue(Objects.requireNonNull(restTemplate.getForObject(uri, ObjectNode.class)));
    }

    private URI buildURI(String event, String service, String keyword) {
        URI uri;
        log.info("Inside buildURI with service : {} and keyword {} ", service, keyword );
        if (keyword == null) {
            uri = UriComponentsBuilder.fromHttpUrl(feeApiBaseUri + feesLookupEndpoint)
                .queryParam("channel", "default")
                .queryParam("event", event)
                .queryParam("jurisdiction1", "family")
                .queryParam("jurisdiction2", "family court")
                .queryParam("service", service)
                .build().toUri();
        } else {
            uri = UriComponentsBuilder.fromHttpUrl(feeApiBaseUri + feesLookupEndpoint)
                .queryParam("channel", "default")
                .queryParam("event", event)
                .queryParam("jurisdiction1", "family")
                .queryParam("jurisdiction2", "family court")
                .queryParam("service", service)
                .queryParam("keyword", keyword)
                .build().toUri();
        }
        log.info("Inside buildURI returning uri {} ", uri.toString() );
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
        String[][] feesItems = {
            {EVENT_ISSUE, SERVICE_DIVORCE, (feesPayKeywords ? KEYWORD_DIVORCE_APPLICATION : null)},
            {EVENT_ISSUE, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_AMEND_PETITION : KEYWORD_ORIGINAL_AMEND)},
            {EVENT_GENERAL_APPLICATION, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_DECREE_NISI : null)},
            {EVENT_ENFORCEMENT, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_BAILIFF_SERVICE : KEYWORD_ORIGINAL_BAILIFF)},
            {EVENT_MISCELLANEOUS, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_FINANCIAL_ORDER : KEYWORD_ORIGINAL_FO)},
            {EVENT_GENERAL_APPLICATION, SERVICE_OTHER, KEYWORD_WITHOUT_NOTICE},
            {EVENT_ISSUE, SERVICE_OTHER, (feesPayKeywords ? KEYWORD_ORIGINATE_PROCEEDINGS : KEYWORD_ORIGINAL_DEFEND)}
        };
        return Stream.of(feesItems).map(i -> getFee(i[0], i[1], i[2])).collect(Collectors.toList());
    }
}
