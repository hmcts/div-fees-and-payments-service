package uk.gov.hmcts.reform.divorce.feepayment.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FeesAndPaymentsServiceHealthCheck extends WebServiceHealthCheck {
    @Autowired
    public FeesAndPaymentsServiceHealthCheck(HttpEntityFactory httpEntityFactory,
                                             @Qualifier("healthCheckRestTemplate") RestTemplate restTemplate,
                                             @Value("${fee.api.baseUri}${fee.api.health}") String uri) {
        super(httpEntityFactory, restTemplate, uri);
    }
}
