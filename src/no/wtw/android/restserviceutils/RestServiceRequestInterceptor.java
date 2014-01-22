package no.wtw.android.restserviceutils;

import android.util.Log;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestServiceRequestInterceptor implements ClientHttpRequestInterceptor {

    private final String TAG = RestServiceRequestInterceptor.class.getSimpleName();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
        Log.d(TAG, request.getMethod().name() + ": " + request.getURI().toString());
        return execution.execute(request, data);
    }

}
