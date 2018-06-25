package uk.gov.hmcts.reform.divorce.feepayment.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeePaymentServiceControllerTest {

    @Mock
    private FeePaymentService feePaymentService;

    @InjectMocks
    private FeePaymentServiceController validationServiceController;

    @Test
    public void givenCoreCaseData_whenValidateIsCalled_thenReturnValidationResult() {

        Fee fee = Fee.builder().feeCode("XXX").amount(Double.valueOf("200")).build();
        when(feePaymentService.getFee("issue")).thenReturn(fee);
        validationServiceController.getFee("issue");
        verify(feePaymentService, times(1)).getFee(any(String.class));
    }

}
