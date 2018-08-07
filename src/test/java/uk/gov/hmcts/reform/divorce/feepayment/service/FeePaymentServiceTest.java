package uk.gov.hmcts.reform.divorce.feepayment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.divorce.feepayment.FeesPaymentServiceApplication;
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;
import uk.gov.hmcts.reform.divorce.feepayment.service.impl.FeePaymentServiceImpl;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

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
    public void setup() throws IOException {
        assertNotNull(feePaymentService);
        ReflectionTestUtils.setField(feePaymentService, FEE_API_FIELD_NAME, FEE_API_URL);
        File file = ResourceUtils.getFile(CLASSPATH_URL_PREFIX + "fee.json");
        ObjectNode objectNode = new ObjectMapper().readValue(file, ObjectNode.class);
        Mockito.when(restTemplate.getForObject(Mockito.eq(uri), Mockito.eq(ObjectNode[].class)))
                .thenReturn(new ObjectNode[]{objectNode});
    }

    public FeePaymentService getFeePaymentService() {
        return feePaymentService;
    }

    @Test
    public void testGetFeeOnIssueEvent() {
        Fee expected = Fee.builder()
                .amount(550.0)
                .feeCode("FEE0002")
                .version(4)
                .description("Filing an application for a divorce, "
                + "nullity or civil partnership dissolution – fees order 1.2.")
                .build();

        Fee actual = feePaymentService.getFee("issue");
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(restTemplate, times(1)).getForObject(Mockito.eq(uri),
                Mockito.eq(ObjectNode[].class));
    }
}
