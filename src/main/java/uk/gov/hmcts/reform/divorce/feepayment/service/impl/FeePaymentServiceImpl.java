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

    private String feeAmount = "fee_amount";
    private String version = "version";
    private String code = "code";
    private String description = "description";

    private String serviceOther = "other";
    private String serviceDivorce = "divorce";

    private String eventIssue = "issue";
    private String eventGeneralApplication = "general application";
    private String eventEnforcement = "enforcement";
    private String eventMiscellaneous = "miscellaneous";

    private String keywordDivorceApplication = "DivorceCivPart";
    private String keywordAmendPetition = "DivorceAmendPetition";
    private String keywordOriginateProceedings = "AppnPrivateOther";
    private String keywordDecreeNisi = "GAContestedOrder";
    private String keywordBailiffService = "BailiffServeDoc";
    private String keywordFinancialOrder = "FinancialOrderOnNotice";
    private String keywordWithoutNotice = "GeneralAppWithoutNotice";
    protected String keywordOriginalAmend = "ABC";
    protected String keywordOriginalDefend = "PQR";
    protected String keywordOriginalBailiff = "HIJ";
    protected String keywordOriginalFo = "financial-order";

    private final String feesLookupEndpoint;
    private Boolean feesPayKeywords;
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
        this.feesPayKeywords = feesPayKeywords;
    }

    @Override
    public Fee getFee(String event, String service, String keyword) {
        URI uri = buildURI(event, service, keyword);
        return getFromRegister(uri);
    }

    @Override
    public Fee getIssueFee() {
        return getFee(eventIssue, serviceDivorce, (feesPayKeywords ? keywordDivorceApplication : null));
    }

    @Override
    public Fee getAmendPetitionFee() {
        return getFee(eventIssue, serviceOther, (feesPayKeywords ? keywordAmendPetition : keywordOriginalAmend));
    }

    @Override
    public Fee getDefendPetitionFee() {
        return getFee(eventIssue, serviceOther, (feesPayKeywords ? keywordOriginateProceedings : keywordOriginalDefend));
    }

    @Override
    public Fee getGeneralApplicationFee() {
        return getFee(eventGeneralApplication, serviceOther, (feesPayKeywords ? keywordDecreeNisi : null));
    }

    @Override
    public Fee getEnforcementFee() {
        return getFee(eventEnforcement, serviceOther, (feesPayKeywords ? keywordBailiffService : keywordOriginalBailiff));
    }

    @Override
    public Fee getApplicationFinancialOrderFee() {
        return getFee(eventMiscellaneous, serviceOther, (feesPayKeywords ? keywordFinancialOrder : keywordOriginalFo));
    }

    @Override
    public Fee getApplicationWithoutNoticeFee() {
        return getFee(eventGeneralApplication, serviceOther, keywordWithoutNotice);
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
        double amount = objectNode.path(feeAmount).asDouble();
        int version = objectNode.path(this.version).asInt();
        String feeCode = objectNode.path(code).asText();
        String description = objectNode.path(this.description).asText();

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
            {eventIssue, serviceDivorce, (feesPayKeywords ? keywordDivorceApplication : null)},
            {eventIssue, serviceOther, (feesPayKeywords ? keywordAmendPetition : keywordOriginalAmend)},
            {eventGeneralApplication, serviceOther, (feesPayKeywords ? keywordDecreeNisi : null)},
            {eventEnforcement, serviceOther, (feesPayKeywords ? keywordBailiffService : keywordOriginalBailiff)},
            {eventMiscellaneous, serviceOther, (feesPayKeywords ? keywordFinancialOrder : keywordOriginalFo)},
            {eventGeneralApplication, serviceOther, keywordWithoutNotice},
            {eventIssue, serviceOther, (feesPayKeywords ? keywordOriginateProceedings : keywordOriginalDefend)}
        };
        return Stream.of(feesItems).map(i -> getFee(i[0], i[1], i[2])).collect(Collectors.toList());
    }
}
