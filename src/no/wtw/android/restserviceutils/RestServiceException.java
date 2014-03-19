package no.wtw.android.restserviceutils;

import android.util.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

public class RestServiceException extends IOException {

    private static final String TAG = RestServiceException.class.getSimpleName();
    private HttpStatus statusCode;
    private RestServiceErrorObject errorObject;

    public RestServiceException(HttpStatus statusCode, String message, RestServiceErrorObject errorObject) {
        this(statusCode, message);
        this.errorObject = errorObject;
    }

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
        if (cause != null && cause instanceof RestServiceException)
            return (RestServiceException) cause;
        if (e instanceof RestServiceException)
            return (RestServiceException) e;

        if (e instanceof HttpMessageConversionException)
            return new RestServiceException(HttpStatus.NO_CONTENT, "Could not parse content");

        if (e instanceof ResourceAccessException)
            return new RestServiceException(HttpStatus.SERVICE_UNAVAILABLE, "Could not access remote server");

        if (e instanceof RestClientException)
            return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown client error");

        return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public RestServiceErrorObject getErrorObject() {
        return errorObject;
    }
}