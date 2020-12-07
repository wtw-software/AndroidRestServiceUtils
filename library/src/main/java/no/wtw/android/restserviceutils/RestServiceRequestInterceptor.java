package no.wtw.android.restserviceutils;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Locale;

public class RestServiceRequestInterceptor implements ClientHttpRequestInterceptor {

    private final RestServiceAPI api;

    public RestServiceRequestInterceptor(RestServiceAPI api) {
        super();
        this.api = api;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        if (headers.getUserAgent().isEmpty())
            headers.setUserAgent(UserAgentFormatter.getUserAgent(api.getContext()));
        headers.setAcceptLanguage(Locale.getDefault().getLanguage());
        HttpAuthentication authentication = api.getAuthentication();
        if (authentication != null)
            headers.setAuthorization(authentication);

        return execution.execute(request, data);
    }

}
