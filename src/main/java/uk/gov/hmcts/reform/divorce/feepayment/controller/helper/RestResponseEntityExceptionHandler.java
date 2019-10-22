package uk.gov.hmcts.reform.divorce.feepayment.controller.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        log.warn(exception.getMessage(), exception);
        return handleExceptionInternal(exception, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = { HttpClientErrorException.class })
    protected ResponseEntity<Object> feeNotFound(RuntimeException exception, WebRequest request) {
        log.warn(exception.getMessage(), exception);
        return handleExceptionInternal(exception, null,
                new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }
}
