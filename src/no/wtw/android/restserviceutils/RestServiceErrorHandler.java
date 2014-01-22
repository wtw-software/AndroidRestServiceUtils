package no.wtw.android.restserviceutils;

import android.text.TextUtils;
import android.util.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
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
        HttpHeaders headers = response.getHeaders();
        if (headers != null) {
            List<String> messageList = headers.get(getErrorMessageHeader());
            String message = messageList == null ? "" : TextUtils.join("\n", messageList);
            HttpStatus responseCode;
            try {
                responseCode = response.getStatusCode();
            } catch (IOException e) {
                Log.e(TAG, "Response code not found in reply");
                responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            throw new RestServiceException(responseCode, message);
        }
        throw new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error, headers missing");
    }

    protected String getErrorMessageHeader() {
        return ERROR_MESSAGE_HEADER;
    }
}