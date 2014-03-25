package no.wtw.android.restserviceutils;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

public class RestServiceErrorHandler implements ResponseErrorHandler {

    public static final String ERROR_MESSAGE_HEADER = "x-wtw-errormessage";
    private static final String TAG = RestServiceErrorHandler.class.getSimpleName();
    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws RestServiceException {
        throw new RestServiceException(
                getResponseCode(response),
                getErrorMessage(response),
                getErrorObject(response));
    }

    private HttpStatus getResponseCode(ClientHttpResponse response) {
        try {
            return response.getStatusCode();
        } catch (IOException e) {
            Log.e(TAG, "Response code not found in reply");
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private String getErrorMessage(ClientHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        if (headers != null) {
            List<String> messageList = headers.get(getErrorMessageHeader());
            return messageList == null ? "" : TextUtils.join("\n", messageList);
        }
        return "Unknown error, headers missing";
    }

    private RestServiceErrorObject getErrorObject(ClientHttpResponse response) {
        StringWriter writer = new StringWriter();
        try {
            InputStream stream = response.getBody();
            IOUtils.copy(stream, writer, "UTF-8");
            if (stream != null)
                stream.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read error body");
            Log.e(TAG, e.getMessage());
        }
        try {
            return new Gson().fromJson(writer.toString(), RestServiceErrorObject.class);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Failed to parse RestServiceErrorObject");
            return new RestServiceErrorObject(writer.toString());
        }
    }

    protected String getErrorMessageHeader() {
        return ERROR_MESSAGE_HEADER;
    }
}