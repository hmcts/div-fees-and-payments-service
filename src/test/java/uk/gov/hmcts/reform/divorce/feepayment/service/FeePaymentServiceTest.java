package uk.gov.hmcts.reform.divorce.feepayment.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FeePaymentServiceTest {

    @Autowired
    private FeePaymentService feePaymentService;


    @Before
    public void setup() {
        assertNotNull(feePaymentService);
    }

    public FeePaymentService getFeePaymentService() {
        return feePaymentService;
    }

    @Test
    public void testGetFees() {
        assertNotNull(feePaymentService.getFee("id"));
    }
}
