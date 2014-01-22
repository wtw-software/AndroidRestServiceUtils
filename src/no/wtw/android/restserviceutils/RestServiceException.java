package no.wtw.android.restserviceutils;

import android.util.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

public class RestServiceException extends IOException {

    private static final String TAG = RestServiceException.class.getSimpleName();
    private HttpStatus statusCode;

    public RestServiceException(HttpStatus statusCode, String message, Throwable cause) {
        this(statusCode, message);
        initCause(cause);
    }

    public RestServiceException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode == null ? HttpStatus.I_AM_A_TEAPOT : statusCode;
        Log.e(TAG, statusCode.toString() + " " + statusCode.name() + " - " + message);
        if (getCause() != null)
            Log.e(TAG, getCause().toString());
    }

    public static RestServiceException getInstance(Exception e) {
        Exception cause = (Exception) e.getCause();
        if (cause != null && cause instanceof RestServiceException) {
            return (RestServiceException) cause;
        } else if (e instanceof RestServiceException) {
            return (RestServiceException) e;
        } else if (e instanceof RestClientException) {
            return new RestServiceException(HttpStatus.SERVICE_UNAVAILABLE, "Could not connect", e);
        } else if (e instanceof HttpMessageConversionException) {
            return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error", e);
        } else {
            return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }


}
