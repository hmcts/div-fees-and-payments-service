package uk.gov.hmcts.reform.divorce.feepayment.service;

import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;

public interface FeePaymentService {

    Fee getFee(String request);

}
