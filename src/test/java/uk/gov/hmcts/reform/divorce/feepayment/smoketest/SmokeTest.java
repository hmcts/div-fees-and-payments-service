package uk.gov.hmcts.reform.divorce.feepayment.smoketest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.divorce.feepayment.controller.FeePaymentServiceController;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    private FeePaymentServiceController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
