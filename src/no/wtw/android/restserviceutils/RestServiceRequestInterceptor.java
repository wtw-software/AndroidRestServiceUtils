package no.wtw.android.restserviceutils;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;

public class RestServiceRequestInterceptor implements ClientHttpRequestInterceptor {

    private final String TAG = RestServiceRequestInterceptor.class.getSimpleName();
    private final RestServiceAPI api;

    public RestServiceRequestInterceptor(RestServiceAPI api) {
        super();
        this.api = api;
        this.api.getRestTemplate().setRequestFactory(
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
        );
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
        Log.d(TAG, request.getMethod().name() + ": " + request.getURI().toString());
        HttpHeaders headers = request.getHeaders();
        headers.setUserAgent(UserAgentFormatter.getUserAgent(api.getContext()));
        headers.setDate(new Date().getTime());
        headers.setAcceptLanguage(Locale.getDefault().getLanguage());
        HttpAuthentication authentication = api.getAuthentication();
        if (authentication != null)
            headers.setAuthorization(authentication);

        ClientHttpResponse response = execution.execute(request, data);
        logRequest(request, data, response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] data, ClientHttpResponse response) throws IOException {
        Log.d(TAG, "Data: " + new String(data));
        for (Object key : request.getHeaders().keySet())
            Log.d(TAG, "<" + key + ">: " + request.getHeaders().get(key));
        if (response != null) {
            Log.d(TAG, "Status: " + response.getStatusCode() + " " + response.getStatusText());
            if (response.getBody() != null)
                Log.d(TAG, "Response: \n" + prettyJson(IOUtils.toString(response.getBody())) + "\n");
        }
    }

    public static String prettyJson(String body) {
        if (TextUtils.isEmpty(body))
            return body;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("\u00A0\u00A0");
            JsonElement jsonElement = new JsonParser().parse(body);
            gson.toJson(jsonElement, jsonWriter);
            return stringWriter.toString();
        } catch (JsonParseException e) {
            return body;
        }
    }

}
