package uk.gov.hmcts.reform.divorce.feepayment.test;

import io.restassured.RestAssured;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import net.thucydides.junit.annotations.TestData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;

import static io.restassured.RestAssured.baseURI;
import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.core.Is.isA;

@Lazy
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = {"uk.gov.hmcts.reform.divorce.feepayment.test", "uk.gov.hmcts.auth.provider.service"})
@ImportAutoConfiguration({HttpMessageConvertersAutoConfiguration.class,
        FeignAutoConfiguration.class})
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
public class FeePaymentIntegrationTest {

    @Value("${fees-and-payments.baseUrl}")
    private String feesPaymentsServiceUrl;

    private String feeEndpoint;

    @Rule
    public SpringIntegrationMethodRule springMethodIntegration = new SpringIntegrationMethodRule();

    @Before
    public void setup() {
        baseURI = feesPaymentsServiceUrl;
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

    @ParameterizedTest
    @MethodSource("data")
    public void feeTest(String feeEndpoint) {
        RestAssured.useRelaxedHTTPSValidation();
        when()
                .get(feeEndpoint)
                .then()
                .assertThat().statusCode(200)
                .and().body("feeCode", isA(String.class));
    }
}
