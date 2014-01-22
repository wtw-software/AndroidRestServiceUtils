package no.wtw.android.restserviceutils;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public abstract class RestServiceAPI {

    public void setInterceptor() {
        ClientHttpRequestInterceptor i = new RestServiceRequestInterceptor();
        getRestTemplate().setInterceptors(Arrays.asList(i));
    }

    public void setErrorHandler() {
        getRestTemplate().setErrorHandler(new RestServiceErrorHandler());
    }

    public RestTemplate getRestTemplate() {
        throw new RuntimeException("getRestTemplate() must be implemented in a sub class.");
    }

    public RestServiceAuthentication getAuthentication(RestServiceCredentialProvider provider) throws RestServiceException {
        return new RestServiceAuthentication(provider);
    }

    public void setAuthentication(RestServiceCredentialProvider provider) throws RestServiceException {
        throw new RuntimeException("setAuthentication() must be implemented in a sub class");
    }

}
