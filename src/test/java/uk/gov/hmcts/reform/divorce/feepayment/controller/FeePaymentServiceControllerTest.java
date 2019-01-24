package uk.gov.hmcts.reform.divorce.feepayment.controller;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }

    @Test
    public void whenGetIssueFeeIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getIssueFee()).thenReturn(expected);
        Fee actual = feePaymentServiceController.lookupFeesForPetitionIssue().getBody();
        verify(feePaymentService, times(1)).getIssueFee();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetAmendFeeIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getAmendPetitionFee()).thenReturn(expected);
        Fee actual = feePaymentServiceController.getAmendFee().getBody();
        verify(feePaymentService, times(1)).getAmendPetitionFee();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetGeneralApplicationFeeIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getGeneralApplicationFee()).thenReturn(expected);
        Fee actual = feePaymentServiceController.getGeneralApplicationFee().getBody();
        verify(feePaymentService, times(1)).getGeneralApplicationFee();
        assertEquals(expected, actual);
    }


    @Test
    public void whenGetDefendFeeIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getDefendPetitionFee()).thenReturn(expected);
        Fee actual = feePaymentServiceController.getDefendedPetitionFee().getBody();
        verify(feePaymentService, times(1)).getDefendPetitionFee();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetEnforcementFeeIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getEnforcementFee()).thenReturn(expected);
        Fee actual = feePaymentServiceController.getEnforcementFee().getBody();
        verify(feePaymentService, times(1)).getEnforcementFee();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetApplicationFinOrderFeeIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getApplicationFinancialOrderFee()).thenReturn(expected);
        Fee actual = feePaymentServiceController.getFinancialOrderFee().getBody();
        verify(feePaymentService, times(1)).getApplicationFinancialOrderFee();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetApplicationWithNoticeFeeIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getApplicationWithoutNoticeFee()).thenReturn(expected);
        Fee actual = feePaymentServiceController.getApplicationWithoutNoticeFee().getBody();
        verify(feePaymentService, times(1)).getApplicationWithoutNoticeFee();
        assertEquals(expected, actual);
    }

    @Test
    public void whenGetAllFeesIsCalled_thenReturnFeeResult() {
        when(feePaymentService.getAllFees()).thenReturn(Arrays.asList(expected));
        List<Fee> actual = feePaymentServiceController.getAllFees().getBody();
        verify(feePaymentService, times(1)).getAllFees();
        assertEquals(Arrays.asList(expected), actual);
    }

}
