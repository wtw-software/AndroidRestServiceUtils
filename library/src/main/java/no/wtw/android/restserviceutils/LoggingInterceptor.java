package no.wtw.android.restserviceutils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private final String TAG = LoggingInterceptor.class.getSimpleName();

    public LoggingInterceptor(RestServiceAPI api) {
        super();
        api.getRestTemplate().setRequestFactory(
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())
        );
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
        Log.d(TAG, request.getMethod().name() + ": " + request.getURI().toString());
        ClientHttpResponse response = execution.execute(request, data);
        logRequest(request, data, response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] data, ClientHttpResponse response) throws IOException {
        Log.v(TAG, "Data: " + new String(data));
        for (Object key : request.getHeaders().keySet())
            Log.v(TAG, "<" + key + ">: " + request.getHeaders().get(key));
        if (response != null) {
            Log.v(TAG, "Status: " + response.getStatusCode() + " " + response.getStatusText());
            if (response.getBody() != null)
                Log.v(TAG, "Response: \n" + prettyJson(IOUtils.toString(response.getBody(), Charset.defaultCharset())) + "\n");
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
