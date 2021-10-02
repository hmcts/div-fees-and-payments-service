package uk.gov.hmcts.reform.divorce.feepayment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    protected FeePaymentServiceImpl feePaymentService;
    private URI issueUrl;
    private URI amendUrl;
    private URI defendUrl;
    private URI generalApplicationUrl;
    private URI enforcementUrl;
    private URI applicationFinOrderUrl;
    private final String applicationWithoutNoticeUrl = "http://feeApiUrl/fees?channel=default&event=general%20application&jurisdiction1=family"
        + "&jurisdiction2=family%20court&service=other&keyword=GeneralAppWithoutNotice";


    @Before
    public void setUpFeePaymentService() {
        setUpUrls(Boolean.TRUE);
        assertNotNull(feePaymentService);
    }

    protected void setUpUrls(boolean featureToggleKeywords) {
        issueUrl = URI.create("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family"
            + "&jurisdiction2=family%20court&service=divorce" + (featureToggleKeywords ? "&keyword=DivorceCivPart" : ""));
        amendUrl = URI.create("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family"
            + "&jurisdiction2=family%20court&service=other" + (featureToggleKeywords ? "&keyword=DivorceAmendPetition" : "&keyword=ABC"));
        defendUrl = URI.create("http://feeApiUrl/fees?channel=default&event=issue&jurisdiction1=family"
            + "&jurisdiction2=family%20court&service=other" + (featureToggleKeywords ? "&keyword=AppnPrivateOther" : "&keyword=PQR"));
        generalApplicationUrl = URI.create("http://feeApiUrl/fees?channel=default&event=general%20application"
            + "&jurisdiction1=family&jurisdiction2=family%20court&service=other" + (featureToggleKeywords ? "&keyword=GAContestedOrder" : ""));
        enforcementUrl = URI.create("http://feeApiUrl/fees?channel=default&event=enforcement&jurisdiction1=family&jurisdiction2=family%20court"
            + "&service=other" + (featureToggleKeywords ? "&keyword=BailiffServeDoc" : "&keyword=HIJ"));
        applicationFinOrderUrl = URI.create("http://feeApiUrl/fees?channel=default&event=miscellaneous&jurisdiction1=family"
            + "&jurisdiction2=family%20court&service=other"
            + (featureToggleKeywords ? "&keyword=FinancialOrderOnNotice" : "&keyword=financial-order"));
        feePaymentService = new FeePaymentServiceImpl(restTemplate, "http://feeApiUrl", "/fees", featureToggleKeywords);
    }

    @Test
    public void testGetFeeOnIssueEvent() throws IOException {
        mockRestTemplate(issueUrl);
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
        URI applicationWithoutNoticeUrl = URI.create(this.applicationWithoutNoticeUrl);
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

    private void mockRestTemplate(URI uri) throws IOException {
        File file = ResourceUtils.getFile(CLASSPATH_URL_PREFIX + "fee.json");
        ObjectNode objectNode = new ObjectMapper().readValue(file, ObjectNode.class);
        Mockito.when(restTemplate.getForObject(Mockito.eq(uri), Mockito.eq(ObjectNode.class)))
            .thenReturn(objectNode);
    }
}
