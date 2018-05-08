package uk.gov.hmcts.reform.divorce.feepayment.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeePaymentServiceControllerTest {

    @Mock
    private FeePaymentService feePaymentService;

    @InjectMocks
    private FeePaymentServiceController validationServiceController;

    @Test
    public void givenCoreCaseData_whenValidateIsCalled_thenReturnValidationResult() {

        when(feePaymentService.getFee("id")).thenReturn("true");
        validationServiceController.getFee("id");
        verify(feePaymentService, times(1)).getFee(any(String.class));
    }

}
