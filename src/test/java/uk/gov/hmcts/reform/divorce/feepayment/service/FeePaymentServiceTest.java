package uk.gov.hmcts.reform.divorce.feepayment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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

    @Value("${fee.api.genAppWithoutNoticeFeeKeyword}")
    private String genAppWithoutNoticeFeeKeyword;

    @InjectMocks
    private FeePaymentServiceImpl feePaymentService;

    private final URI issueUrl = URI.create("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family"
            + "&jurisdiction2=family%20court&service=divorce&keyword=DivorceCivPart");

    private final URI amendUrl = URI.create("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family"
        + "&jurisdiction2=family%20court&service=other&keyword=DivorceAmendPetition");

    private final URI defendUrl = URI.create("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family"
        + "&jurisdiction2=family%20court&service=other&keyword=Private");

    private final URI generalApplicationUrl = URI.create("http://feeApiUrl/fees?channel=default&event=general%20application"
        + "&jurisdiction1=family" + "&jurisdiction2=family%20court&service=other&keyword=GAContestedOrder");

    private final URI enforcementUrl = URI.create("http://feeApiUrl/fees?channel=default&event=enforcement"
        + "&jurisdiction1=family" + "&jurisdiction2=family%20court&service=other&keyword=BailiffServeDoc");

    private final URI applicationFinOrderUrl = URI.create("http://feeApiUrl/fees?channel=default&event=miscellaneous"
        + "&jurisdiction1=family" + "&jurisdiction2=family%20court&service=other&keyword=FinancialOrderOnNotice");

    private final String applicationWithoutNoticePartialUrl = "http://feeApiUrl/fees?channel=default&event=general%20application"
        + "&jurisdiction1=family" + "&jurisdiction2=family%20court&service=other&keyword=";

    @Before
    public void setup() {
        feePaymentService = new FeePaymentServiceImpl(restTemplate, "http://feeApiUrl", "/fees");
        assertNotNull(feePaymentService);
    }

    private void mockRestTemplate(URI uri) throws IOException {
        File file = ResourceUtils.getFile(CLASSPATH_URL_PREFIX + "fee.json");
        ObjectNode objectNode = new ObjectMapper().readValue(file, ObjectNode.class);
        Mockito.when(restTemplate.getForObject(Mockito.eq(uri), Mockito.eq(ObjectNode.class)))
                .thenReturn(objectNode);
    }

    public FeePaymentService getFeePaymentService() {
        return feePaymentService;
    }

    @Test
    public void testGetFeeOnIssueEvent() throws IOException {
        mockRestTemplate( issueUrl);
        Fee expected = Fee.builder()
                .amount(550.0)
                .feeCode("FEE0002")
                .version(4)
                .description("Filing an application for a divorce, "
                + "nullity or civil partnership dissolution – fees order 1.2.")
                .build();

        Fee actual = feePaymentService.getIssueFee();
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(restTemplate, times(1)).getForObject(Mockito.eq(issueUrl),
                Mockito.eq(ObjectNode.class));
    }

    @Test
    public void testAmendFeeEvent() throws IOException {
        mockRestTemplate(amendUrl);
        feePaymentService.getAmendPetitionFee();
        verify(restTemplate, times(1)).getForObject(Mockito.eq(amendUrl),
            Mockito.eq(ObjectNode.class));
    }

    @Test
    public void testDefendFeeEvent() throws IOException {
        mockRestTemplate(defendUrl);
        feePaymentService.getDefendPetitionFee();
        verify(restTemplate, times(1)).getForObject(Mockito.eq(defendUrl),
            Mockito.eq(ObjectNode.class));
    }

    @Test
    public void testGeneralApplicationFeeEvent() throws IOException {
        mockRestTemplate(generalApplicationUrl);
        feePaymentService.getGeneralApplicationFee();
        verify(restTemplate, times(1)).getForObject(Mockito.eq(generalApplicationUrl),
            Mockito.eq(ObjectNode.class));
    }

    @Test
    public void testEnforcementFeeEvent() throws IOException {
        mockRestTemplate(enforcementUrl);
        feePaymentService.getEnforcementFee();
        verify(restTemplate, times(1)).getForObject(Mockito.eq(enforcementUrl),
            Mockito.eq(ObjectNode.class));
    }

    @Test
    public void testApplicationFinOrderFeeEvent() throws IOException {
        mockRestTemplate(applicationFinOrderUrl);
        feePaymentService.getApplicationFinancialOrderFee();
        verify(restTemplate, times(1)).getForObject(Mockito.eq(applicationFinOrderUrl),
            Mockito.eq(ObjectNode.class));
    }

    @Test
    public void testApplicationWithoutNoticeFeeEvent() throws IOException {
        URI applicationWithoutNoticeUrl = URI.create(applicationWithoutNoticePartialUrl + genAppWithoutNoticeFeeKeyword);
        mockRestTemplate(applicationWithoutNoticeUrl);
        feePaymentService.getApplicationWithoutNoticeFee();
        verify(restTemplate, times(1)).getForObject(Mockito.eq(applicationWithoutNoticeUrl),
            Mockito.eq(ObjectNode.class));
    }

    @Test
    public void testAllFees() throws IOException {
        File file = ResourceUtils.getFile(CLASSPATH_URL_PREFIX + "fee.json");
        ObjectNode objectNode = new ObjectMapper().readValue(file, ObjectNode.class);
        Mockito.when(restTemplate.getForObject(Mockito.any(), Mockito.eq(ObjectNode.class)))
            .thenReturn(objectNode);
        feePaymentService.getAllFees();
        verify(restTemplate, times(7)).getForObject(Mockito.any(),
            Mockito.eq(ObjectNode.class));
    }
}
