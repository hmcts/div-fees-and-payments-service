package uk.gov.hmcts.reform.divorce.feepayment.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.feepayment.FeesPaymentServiceApplication;
import uk.gov.hmcts.reform.divorce.feepayment.service.impl.FeePaymentServiceImpl;

import java.net.URI;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FeesPaymentServiceApplication.class)
public class FeePaymentServiceTest {

    @Mock
    private RestTemplate restTemplate;
    private String FEE_API_URL = "http://feeApiUrl";
    private String FEE_API_FIELD_NAME = "feeApiUrl";

    @InjectMocks
    private FeePaymentServiceImpl feePaymentService;

    private URI uri = URI.create("http://feeApiUrl?channel=default&event=issue&jurisdiction1=family&jurisdiction2=family%20court&service=divorce");

    @Before
    public void setup() {
        assertNotNull(feePaymentService);
        ReflectionTestUtils.setField(feePaymentService, FEE_API_FIELD_NAME, FEE_API_URL);

        Mockito.when(restTemplate.getForObject(Mockito.eq(uri), Mockito.eq(ObjectNode[].class))).thenReturn(new ObjectNode[]{});
    }

    public FeePaymentService getFeePaymentService() {
        return feePaymentService;
    }

    @Test
    public void testGetFeeOnIssueEvent() {
        assertNotNull(feePaymentService.getFee("issue"));
        verify(restTemplate, times(1)).getForObject(Mockito.eq(uri), Mockito.eq(ObjectNode[].class));
    }
}
