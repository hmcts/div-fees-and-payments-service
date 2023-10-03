package uk.gov.hmcts.reform.divorce.feepayment.smoketest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.divorce.feepayment.controller.FeePaymentServiceController;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    private FeePaymentServiceController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
