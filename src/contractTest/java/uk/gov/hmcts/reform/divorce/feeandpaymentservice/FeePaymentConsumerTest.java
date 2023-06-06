package uk.gov.hmcts.reform.divorce.feeandpaymentservice;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactFolder;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.divorce.feepayment.FeesPaymentServiceApplication;

import java.io.IOException;

@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@PactTestFor(providerName = "feeRegister_lookUp", port = "4009")
@PactFolder("build/pacts")
@SpringBootTest(classes = FeesPaymentServiceApplication.class)
public class FeePaymentConsumerTest {

    @Value("${feature-toggle.toggle.fee-pay-keywords}")
    private Boolean featureToggleKeywords;

    private static final String BASEURI = "/fees-register/fees/lookup";
    private String petitionIssueFee;
    private String amendFee;
    private String defendantFee;
    private String generalApplicationFee;
    private String enforcementFee;
    private String financialOrderFee;
    private String applicationNoNotice;

    @BeforeEach
    public void setUpEachTest() throws InterruptedException {
        Thread.sleep(2000);

        petitionIssueFee = "channel=default&event=issue&jurisdiction1=family&"
            + "jurisdiction2=family%20court&service=divorce" + (featureToggleKeywords ? "&keyword=DivorceCivPart" : "");

        amendFee = "channel=default&event=issue&jurisdiction1=family&"
            + "jurisdiction2=family%20court&service=other"
            + (featureToggleKeywords ? "&keyword=DivorceAmendPetition" : "&keyword=ABC");

        defendantFee = "channel=default&event=issue&jurisdiction1=family&"
            + "jurisdiction2=family%20court&service=other"
            + (featureToggleKeywords ? "&keyword=AppnPrivateOther" : "&keyword=PQR");

        generalApplicationFee = "channel=default&event=general%20application&jurisdiction1=family&"
            + "jurisdiction2=family%20court&service=other"
            + (featureToggleKeywords ? "&keyword=GAContestedOrder" : "");

        enforcementFee = "channel=default&event=enforcement&jurisdiction1=family&"
            + "jurisdiction2=family%20court&service=other"
            + (featureToggleKeywords ? "&keyword=BailiffServeDoc" : "&keyword=HIJ");

        financialOrderFee = "channel=default&event=miscellaneous&jurisdiction1=family&"
            + "jurisdiction2=family%20court&service=other"
            + (featureToggleKeywords ? "&keyword=FinancialOrderOnNotice" : "&keyword=financial-order");

        applicationNoNotice = "channel=default&event=general%20application&jurisdiction1=family&"
            + "jurisdiction2=family%20court&keyword=GeneralAppWithoutNotice&service=other";
    }

    @Pact(consumer = "divorce_feeAndPaymentService_feeRegister")
    public RequestResponsePact pettitionIssueFee(PactDslWithProvider builder) {

        return builder.given("service is registered in Fee registry")
            .uponReceiving("request for petition issue fee")
            .path(BASEURI)
            .encodedQuery(petitionIssueFee)
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(getFeeResponseBody("FEE0002", 550.00, 4,
                "Filing an application for a divorce, nullity or civil partnership dissolution – fees order 1.2."))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "pettitionIssueFee")
    public void verifyPetitionIssueFeePact(MockServer mockServer) throws IOException, JSONException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + BASEURI + "?" + petitionIssueFee)
            .execute().returnResponse();
    }

    @Pact(consumer = "divorce_feeAndPaymentService_feeRegister")
    public RequestResponsePact getAmendFee(PactDslWithProvider builder) {

        return builder.given("service is registered in Fee registry")
            .uponReceiving("received request for Amend issue fee")
            .path(BASEURI)
            .encodedQuery(amendFee)
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(getFeeResponseBody("FEE0233", 95.00, 1,
                "Amendment of application for matrimonial/civil partnership orde"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getAmendFee")
    public void verifyAmendFeePact(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + BASEURI + "?" + amendFee)
            .execute().returnResponse();
    }

    @Pact(consumer = "divorce_feeAndPaymentService_feeRegister")
    public RequestResponsePact getDefendantFee(PactDslWithProvider builder) {

        return builder.given("service is registered in Fee registry")
            .uponReceiving("received request for Defendant issue fee")
            .path(BASEURI)
            .encodedQuery(defendantFee)
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(getFeeResponseBody("FEE0388", 245.00, 1,
                "Originating proceedings where no other fee is specified"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getDefendantFee")
    public void verifyDefendantFeePact(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + BASEURI + "?" + defendantFee)
            .execute().returnResponse();
    }

    @Pact(consumer = "divorce_feeAndPaymentService_feeRegister")
    public RequestResponsePact generalApplicationFee(PactDslWithProvider builder) {

        return builder.given("service is registered in Fee registry")
            .uponReceiving("received request for General Application fee")
            .path(BASEURI)
            .encodedQuery(generalApplicationFee)
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(getFeeResponseBody("FEE0271", 50.00, 1,
                "Application for decree nisi, conditional order, separation order (no fee if undefended"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "generalApplicationFee")
    public void verifyGeneralApplicationFeePact(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + BASEURI + "?" + generalApplicationFee)
            .execute().returnResponse();
    }

    @Pact(consumer = "divorce_feeAndPaymentService_feeRegister")
    public RequestResponsePact enforcementFee(PactDslWithProvider builder) {

        return builder.given("service is registered in Fee registry")
            .uponReceiving("received request for Enforcement Application fee")
            .path(BASEURI)
            .encodedQuery(enforcementFee)
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(getFeeResponseBody("FEE0392", 45.00, 2,
                "Request for service by a bailiff of document (see order for exceptions)"))
            .toPact();
    }


    @Test
    @PactTestFor(pactMethod = "enforcementFee")
    public void verifyEnforcementFeePact(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + BASEURI + "?" + enforcementFee)
            .execute().returnResponse();
    }

    @Pact(consumer = "divorce_feeAndPaymentService_feeRegister")
    public RequestResponsePact financialOrderFee(PactDslWithProvider builder) {

        return builder.given("service is registered in Fee registry")
            .uponReceiving("received request for Financial Order Application fee")
            .path(BASEURI)
            .encodedQuery(financialOrderFee)
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(getFeeResponseBody("FEE0229", 255.00, 1,
                "Application for a financial orde"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "financialOrderFee")
    public void verifyFinancialOrderFeePact(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + BASEURI + "?" + financialOrderFee)
            .execute().returnResponse();
    }

    @Pact(consumer = "divorce_feeAndPaymentService_feeRegister")
    public RequestResponsePact applicationNoNotice(PactDslWithProvider builder) {

        return builder.given("service is registered in Fee registry")
            .uponReceiving("received request for application no notice fee")
            .path(BASEURI)
            .encodedQuery(applicationNoNotice)
            .method("GET")
            .willRespondWith()
            .status(200)
            .body(getFeeResponseBody("FEE0228", 50.00, 1,
                "Application (without notice)"))
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "applicationNoNotice")
    public void verifyApplicationNoNoticeFeePact(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + BASEURI + "?" + applicationNoNotice)
            .execute().returnResponse();
    }

    private PactDslJsonBody getFeeResponseBody(String code, double amount, int version, String description) {
        return new PactDslJsonBody()
            .stringType("code", code)
            .decimalType("fee_amount", amount)
            .numberType("version", version)
            .stringType("description", description);
    }
}


