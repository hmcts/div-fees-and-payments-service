package uk.gov.hmcts.reform.divorce.feepayment.service;

import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;

import java.util.List;

public interface FeePaymentService {

    Fee getFee(String request, String service, String keyword);

    Fee getIssueFee();

    Fee getAmendPetitionFee();

    Fee getDefendPetitionFee();

    Fee getGeneralApplicationFee();

    Fee getEnforcementFee();

    Fee getApplicationFinancialOrderFee();

    Fee getApplicationWithoutNoticeFee();

    List<Fee> getAllFees();

}
