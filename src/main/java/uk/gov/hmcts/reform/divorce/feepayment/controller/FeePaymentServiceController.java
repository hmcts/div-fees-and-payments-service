package uk.gov.hmcts.reform.divorce.feepayment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.divorce.feepayment.service.FeePaymentService;

import javax.validation.Valid;

@RestController
@Api(value = "Divorce Fee Payment Service", tags = {"Divorce Fee Payment Service"})
@Slf4j
public class FeePaymentServiceController {

    @Autowired
    private FeePaymentService feePaymentService;

    @ApiOperation(value = "Get a valid getFee for a given service", tags = {"Divorce Fee Payment Service"})
    @PostMapping("/version/1/fee")
    public String getFee(@RequestBody @Valid @ApiParam(value = "Fee Request", required = true)
                                               String request) {
        return feePaymentService.getFee(request);
    }
}