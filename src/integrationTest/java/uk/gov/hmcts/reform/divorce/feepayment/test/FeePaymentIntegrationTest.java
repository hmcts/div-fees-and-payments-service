package uk.gov.hmcts.reform.divorce.feepayment.test;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.baseURI;
import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.core.Is.isA;

@Lazy
@RunWith(SerenityRunner.class)
@ComponentScan(basePackages = {"uk.gov.hmcts.reform.divorce.feepayment.test", "uk.gov.hmcts.auth.provider.service"})
@ImportAutoConfiguration({RibbonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
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

    public FeePaymentIntegrationTest(String feeEndpoint) {
        this.feeEndpoint = feeEndpoint;
    }

    @Parameterized.Parameters
    public static String[]data() {
        return new String[] {
            "/get-all-fees",
            "/petition-issue-fee",
            "/general-application-fee",
            "/enforcement-fee",
            "/application-financial-order-fee",
            "application-without-notice-fee",
            "/amend-fee",
            "/defended-petition-fee"
        };
    }

    @Test
    public void feeTest() {
        when()
            .get(feeEndpoint)
            .then()
            .assertThat().statusCode(200)
            .and().body("feeCode", isA(String.class));
    }
}
