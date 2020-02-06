package uk.gov.hmcts.reform.divorce.feepayment.health;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DefaultHttpEntityFactoryUTest {

    private DefaultHttpEntityFactory classUndertest = new DefaultHttpEntityFactory();

    @Test
    public void shouldCreateRequestEntityForHealthCheck() {

        HttpEntity<Object> actual = classUndertest.createRequestEntityForHealthCheck();

        assertNotNull(actual);
        assertNotNull(actual.getHeaders());
        assertEquals(actual.getHeaders().getAccept(), Collections.singletonList(MediaType.APPLICATION_JSON));
    }
}
