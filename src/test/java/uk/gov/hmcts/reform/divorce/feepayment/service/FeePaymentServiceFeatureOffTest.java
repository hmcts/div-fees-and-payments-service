package uk.gov.hmcts.reform.divorce.feepayment.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FeePaymentServiceFeatureOffTest extends FeePaymentServiceTest {

    @Before
    @Override
    public void setUpFeePaymentService() {
        setUpUrls(Boolean.FALSE);
        assertNotNull(feePaymentService);
    }

    @Test
    @Override
    public void testUrls() {
        assertEquals("http://feeApiUrl/fees?channel=default&event=general%20application&jurisdiction1=family&jurisdiction2=family%20court&service=other", generalApplicationUrl.toString());
        assertEquals("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family&jurisdiction2=family%20court&service=divorce", issueUrl.toString());
        assertEquals("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family&jurisdiction2=family%20court&service=other&keyword=ABC",  amendUrl.toString());
        assertEquals("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family&jurisdiction2=family%20court&service=other&keyword=PQR",  defendUrl.toString());
        assertEquals("http://feeApiUrl/fees?channel=default&event=enforcement&jurisdiction1=family&jurisdiction2=family%20court&service=other&keyword=HIJ",  enforcementUrl.toString());
        assertEquals("http://feeApiUrl/fees?channel=default&event=miscellaneous&jurisdiction1=family&jurisdiction2=family%20court&service=other&keyword=financial-order",  applicationFinOrderUrl.toString());
        assertEquals("http://feeApiUrl/fees?channel=default&event=general%20application&jurisdiction1=family&jurisdiction2=family%20court&service=other&keyword=GeneralAppWithoutNotice",  applicationWithoutNoticeUrl.toString());
    }
}
