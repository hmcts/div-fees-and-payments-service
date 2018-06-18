package uk.gov.hmcts.reform.divorce.feepayment.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/fees-and-payments")
public class FeePaymentServiceController {

    private static final String ISSUE = "issue";

    @Autowired
    private FeePaymentService feePaymentService;


    @ApiOperation(value = "Lookup fee for petition issue event", tags = {"Fee Lookup"})
    @GetMapping("/version/1/petition-issue-fee")
    public Fee lookupFeesForPetitionIssue() {
        System.out.println("test");
        return feePaymentService.getFee(ISSUE);
    }

    @ApiOperation(value = "Lookup fee for any event", tags = {"Fee Lookup"})
    @GetMapping("/version/1/fee")
    public Fee getFee(@RequestParam @Valid @ApiParam(value = "Fee Request", required = true)
                              String request) {
        return feePaymentService.getFee(request);
    }

}