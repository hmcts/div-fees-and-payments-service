package uk.gov.hmcts.reform.divorce.feepayment.controller.helper;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class RestResponseEntityExceptionHandlerUTest {

    private final RestResponseEntityExceptionHandler classUnderTest = new RestResponseEntityExceptionHandler();

    @Test
    public void whenHandleNoResponseException_thenReturnNoResponse() {
        final RuntimeException exception = new RuntimeException();
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = classUnderTest.feeNotFound(exception, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void whenHandleConflictException_thenReturnConflict() {
        final RuntimeException exception = new RuntimeException();
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> response = classUnderTest.handleConflict(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
