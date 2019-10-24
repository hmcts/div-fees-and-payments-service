package uk.gov.hmcts.reform.divorce.feepayment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Fee {

    String feeCode;
    double amount;
    int  version;
    String description;
}
