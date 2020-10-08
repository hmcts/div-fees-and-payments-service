package uk.gov.hmcts.reform.divorce.feepayment.test;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.serenitybdd.junit.runners.SerenityRunner;
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
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;

import java.util.List;

import static net.serenitybdd.rest.SerenityRest.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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

    @Test
    public void checkThatResponseHasAListOfFees() {
        // Option 1
//        final String  responseStr =
//            when()
//              .get(feesPaymentsServiceUrl + "/get-all-fees")
//              .then()
//              .extract().body().asString();
//
//        final Gson gson = new Gson();
//        final List<Fee> fee = gson.fromJson(responseStr, new TypeToken<List<Fee>>(){}.getType());
//
//        assertThat(fee.size() ,  greaterThan(1) ) ;
//        assertThat(fee.get(0).getFeeCode() ,  isA(String.class)) ;

        // Option 2
        final List<Fee> feeList =
            when()
              .get(feesPaymentsServiceUrl + "/get-all-fees")
              .then()
              .extract().jsonPath().getList("Fee", Fee.class);

        assertThat(feeList.size() , greaterThan(1) ) ;
        assertThat(feeList.get(0).getFeeCode() , isA(String.class));



    }
}
