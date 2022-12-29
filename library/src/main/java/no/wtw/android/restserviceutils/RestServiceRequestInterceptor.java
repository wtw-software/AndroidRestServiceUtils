package no.wtw.android.restserviceutils;

import android.text.TextUtils;

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
        if (TextUtils.isEmpty(headers.getUserAgent()))
            headers.setUserAgent(UserAgentFormatter.getUserAgent(api.getContext()));
        headers.setAcceptLanguage(Locale.getDefault().getLanguage());
        return execution.execute(request, data);
    }

}
