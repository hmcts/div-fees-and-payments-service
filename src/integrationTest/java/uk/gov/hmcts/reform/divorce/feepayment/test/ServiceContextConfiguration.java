package uk.gov.hmcts.reform.divorce.feepayment.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Lazy
@PropertySource("classpath:application.properties")
public class ServiceContextConfiguration {

}
