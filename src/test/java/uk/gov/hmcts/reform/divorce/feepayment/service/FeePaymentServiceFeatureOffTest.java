package uk.gov.hmcts.reform.divorce.feepayment.service;

import org.junit.Before;

import static org.junit.Assert.assertNotNull;

public class FeePaymentServiceFeatureOffTest extends FeePaymentServiceTest {

    @Before
    @Override
    public void setUpFeePaymentService() {
        setUpUrls(Boolean.FALSE);
        assertNotNull(feePaymentService);
    }
}
