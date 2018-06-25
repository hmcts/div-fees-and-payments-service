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
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.feepayment.FeesPaymentServiceApplication;
import uk.gov.hmcts.reform.divorce.feepayment.service.impl.FeePaymentServiceImpl;

import java.net.URI;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FeesPaymentServiceApplication.class)
public class FeePaymentServiceTest {

    @Mock
    private RestTemplate restTemplate;
    private String feeApiUrl = "http://feeApiUrl";

    @InjectMocks
    private FeePaymentServiceImpl feePaymentService;
    @Before
    public void setup() {
        assertNotNull(feePaymentService);
        feePaymentService.setFeeApiUrl(feeApiUrl);
        Mockito.when(restTemplate.getForObject(Mockito.any(URI.class), Mockito.any(Class.class))).thenReturn(new ObjectNode[]{});
    }

    public FeePaymentService getFeePaymentService() {
        return feePaymentService;
    }

    @Test
    public void testGetFeeOnIssueEvent() {
        assertNotNull(feePaymentService.getFee("issue"));
    }
}
