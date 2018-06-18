package uk.gov.hmcts.reform.divorce.feepayment.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by mrganeshraja on 13/06/2018.
 */
@Data
@Builder
public class Fee {

    String feeCode;

    double amount;

    int  version;
}
