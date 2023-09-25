package uk.gov.hmcts.reform.divorce.feepayment.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.feepayment.model.Fee;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/fees-and-payments")
public class FeePaymentServiceController {

    @Autowired
    private FeePaymentService feePaymentService;

    @Operation(description = "Get Issue fee", tags = {"Fee Lookup"})
    @GetMapping(value = "/version/1/petition-issue-fee", produces = "application/json")
    public ResponseEntity<Fee> lookupFeesForPetitionIssue() {
        log.info("Getting fee for issue");
        return ResponseEntity.ok(feePaymentService.getIssueFee());
    }

    @Operation(description = "Get amend fee", tags = {"Fee Lookup"})
    @GetMapping("/version/1/amend-fee")
    public ResponseEntity<Fee> getAmendFee()  {
        return ResponseEntity.ok(feePaymentService.getAmendPetitionFee());
    }

    @Operation(description = "Get Defended petition fee", tags = {"Fee Lookup"})
    @GetMapping("/version/1/defended-petition-fee")
    public ResponseEntity<Fee>  getDefendedPetitionFee()  {
        return ResponseEntity.ok(feePaymentService.getDefendPetitionFee());
    }

    @Operation(description = "Get General Application fee", tags = {"Fee Lookup"})
    @GetMapping("/version/1/general-application-fee")
    public ResponseEntity<Fee>  getGeneralApplicationFee()  {
        return ResponseEntity.ok(feePaymentService.getGeneralApplicationFee());
    }

    @Operation(description = "Get Enforcement fee", tags = {"Fee Lookup"})
    @GetMapping("/version/1/enforcement-fee")
    public ResponseEntity<Fee>  getEnforcementFee()  {
        return ResponseEntity.ok(feePaymentService.getEnforcementFee());
    }

    @Operation(description = "Get Application Financial Order fee", tags = {"Fee Lookup"})
    @GetMapping("/version/1/application-financial-order-fee")
    public ResponseEntity<Fee>  getFinancialOrderFee()  {
        return ResponseEntity.ok(feePaymentService.getApplicationFinancialOrderFee());
    }

    @Operation(description = "Get Enforcement fee", tags = {"Fee Lookup"})
    @GetMapping("/version/1/application-without-notice-fee")
    public ResponseEntity<Fee>  getApplicationWithoutNoticeFee()  {
        return ResponseEntity.ok(feePaymentService.getApplicationWithoutNoticeFee());
    }

    @Operation(description = "Get all fees", tags = {"Fee Lookup"})
    @GetMapping("/version/1/get-all-fees")
    public ResponseEntity<List<Fee>> getAllFees() {
        return ResponseEntity.ok(feePaymentService.getAllFees());
    }
}