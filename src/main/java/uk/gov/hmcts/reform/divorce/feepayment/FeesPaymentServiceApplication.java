package uk.gov.hmcts.reform.divorce.feepayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.gov.hmcts.reform.authorisation.healthcheck.ServiceAuthHealthIndicator;

@SpringBootApplication(scanBasePackages = {"uk.gov.hmcts.reform.divorce",
        "uk.gov.hmcts.reform.logging.appinsights" } ,
        exclude = {ServiceAuthHealthIndicator.class})
public class FeesPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeesPaymentServiceApplication.class, args);
    }
}