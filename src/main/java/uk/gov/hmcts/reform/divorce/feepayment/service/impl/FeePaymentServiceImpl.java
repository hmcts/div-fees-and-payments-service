package uk.gov.hmcts.reform.divorce.feepayment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

@Service
@Slf4j
public class FeePaymentServiceImpl implements FeePaymentService {

    @Override
    public String getFee(String request) {
        //TODO:remove the with right implementation.
        return "true";
    }
}