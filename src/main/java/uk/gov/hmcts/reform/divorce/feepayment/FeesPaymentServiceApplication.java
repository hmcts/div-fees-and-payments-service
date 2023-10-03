package uk.gov.hmcts.reform.divorce.feepayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"uk.gov.hmcts.reform.divorce",
        "uk.gov.hmcts.reform.logging.appinsights" })
@EnableConfigurationProperties
public class FeesPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeesPaymentServiceApplication.class, args);
    }
}