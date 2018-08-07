package uk.gov.hmcts.reform.divorce.feepayment.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class FeePaymentServiceControllerTest {

    @Mock
    private FeePaymentService feePaymentService;

    @InjectMocks
    private FeePaymentServiceController feePaymentServiceController;

    private static Fee expected = null;

    @Before
    public void setup() {
        assertNotNull(feePaymentService);
        expected = Fee.builder()
                .feeCode("XXX")
                .amount(Double.valueOf("200"))
                .build();

        when(feePaymentService.getFee("issue")).thenReturn(expected);
    }

    @Test
    public void whenGetFeeIsCalled_thenReturnFeeResult() {
        Fee actual = feePaymentServiceController.getFee("issue");
        verify(feePaymentService, times(1)).getFee(eq("issue"));
        assertEquals(expected, actual);
    }

    @Test
    public void whenLookupFeesForPetitionIssueIsCalled_thenReturnFeeResult() {

        ResponseEntity<Fee> response = feePaymentServiceController.lookupFeesForPetitionIssue();
        assertNotNull(response);
        assertEquals(response.getStatusCode(), ResponseEntity.ok().build().getStatusCode());
        verify(feePaymentService, times(1)).getFee(eq("issue"));
        Fee actual = response.getBody();
        assertEquals(expected, actual);
    }

}
