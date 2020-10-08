package uk.gov.hmcts.reform.divorce.feepayment.test;

import io.restassured.response.Response;
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
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.isA;

@Lazy
@RunWith(SerenityRunner.class)
@ComponentScan(basePackages = {"uk.gov.hmcts.reform.divorce.feepayment.test", "uk.gov.hmcts.auth.provider.service"})
@ImportAutoConfiguration({RibbonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class, FeignAutoConfiguration.class})
@ContextConfiguration(classes = {ServiceContextConfiguration.class})
public class GetAllFeePaymentIntegrationTest {

    @Value("${fees-and-payments.baseUrl}")
    private String feesPaymentsServiceUrl;

    @Rule
    public SpringIntegrationMethodRule springMethodIntegration = new SpringIntegrationMethodRule();

    @Before
    public void setup() {
        baseURI = feesPaymentsServiceUrl;
    }

    @Test
    public void shouldReturnSucessOnServiceCall() {
        when()
            .get(feesPaymentsServiceUrl + "/get-all-fees")
            .then()
            .assertThat().statusCode(200);
    }

    @Test
    public void shouldReturnCorrectFeeCodeType() {

        Response response = when()
                .get(feesPaymentsServiceUrl + "/get-all-fees")
                .then()
                .extract().response();

        List<String> feeCodes = response.getBody().path("feeCode");

        assertThat(feeCodes.get(0), isA(String.class));
        assertThat(feeCodes.get(1), isA(String.class));
        assertThat(feeCodes.get(2), isA(String.class));
    }

}
