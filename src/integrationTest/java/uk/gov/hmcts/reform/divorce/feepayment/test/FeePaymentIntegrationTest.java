package uk.gov.hmcts.reform.divorce.feepayment.test;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.thucydides.junit.annotations.TestData;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.baseURI;
import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.core.Is.isA;

@Lazy
@RunWith(SerenityParameterizedRunner.class)
@ComponentScan(basePackages = {"uk.gov.hmcts.reform.divorce.feepayment.test", "uk.gov.hmcts.auth.provider.service"})
@ImportAutoConfiguration({HttpMessageConvertersAutoConfiguration.class, FeignAutoConfiguration.class})
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
public class FeePaymentIntegrationTest {

    @Value("${fees-and-payments.baseUrl}")
    private String feesPaymentsServiceUrl;

    private String feeEndpoint;

    @Rule
    public SpringIntegrationMethodRule springMethodIntegration = new SpringIntegrationMethodRule();

    @BeforeEach
    public void setup() {
        baseURI = feesPaymentsServiceUrl;
    }

    public FeePaymentIntegrationTest(String feeEndpoint) {
        this.feeEndpoint = feeEndpoint;
    }

    @TestData
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/petition-issue-fee"},
                {"/enforcement-fee"},
                {"/application-financial-order-fee"},
                {"application-without-notice-fee"},
                {"/amend-fee"},
                {"/defended-petition-fee"},
                {"/general-application-fee"}
        });
    }

    @Test
    public void feeTest() {
        RestAssured.useRelaxedHTTPSValidation();
        when()
                .get(feeEndpoint)
                .then()
                .assertThat().statusCode(200)
                .and().body("feeCode", isA(String.class));
    }
}
