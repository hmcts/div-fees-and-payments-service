package uk.gov.hmcts.reform.divorce.feepayment.test;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.junit.spring.integration.SpringIntegrationMethodRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;

import static io.restassured.RestAssured.baseURI;
import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.Is.isA;

@Lazy
@RunWith(SerenityRunner.class)
@ComponentScan(basePackages = {"uk.gov.hmcts.reform.divorce.feepayment.test", "uk.gov.hmcts.auth.provider.service"})
@ImportAutoConfiguration({RibbonAutoConfiguration.class,HttpMessageConvertersAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
@EnableFeignClients(basePackageClasses = ServiceAuthorisationApi.class)
@PropertySource("classpath:application.properties")
@PropertySource("classpath:application-${env}.properties")
public class FeePaymentIntegrationTest {

    @Value("${fees-and-payments.baseUrl}")
    private String feesPaymentsServiceUrl;

    @Rule
    public SpringIntegrationMethodRule springMethodIntegration = new SpringIntegrationMethodRule();

    @Before
    public void setup() {
       baseURI = feesPaymentsServiceUrl;
    }

    @Test
    public void getFeeCodeFeesLookupForIssue() {
        when()
                .get("/petition-issue-fee")
                .then()
                .assertThat().statusCode(200)
                .and().body("feeCode", isA(String.class));
    }

    @Test
    public void getFeeAmtFeesLookupForIssue() {
        when()
                .get("/petition-issue-fee")
                .then().assertThat().statusCode(200)
                .and().body("amount", hasToString("550.0"));
    }

    @Test
    public void getFeeLookupForInvalidEvent() {
        when()
                .get("/fee?request=not_available")
                .then().assertThat().statusCode(204);
    }

    @Test
    public void getFeeLookupValidEvent() {
        when()
                .get("/fee?request=issue")
                .then().assertThat().statusCode(200)
                .and().body("amount", hasToString("550.0"));
    }
}
