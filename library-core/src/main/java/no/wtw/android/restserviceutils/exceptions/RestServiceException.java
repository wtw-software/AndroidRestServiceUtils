package no.wtw.android.restserviceutils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

import no.wtw.android.restserviceutils.RestServiceErrorObject;

public class RestServiceException extends IOException {

    private HttpStatus statusCode;
    private RestServiceErrorObject errorObject;

    public RestServiceException(HttpStatus statusCode, String message, RestServiceErrorObject errorObject) {
        this(message);
        setStatusCode(statusCode);
        this.errorObject = errorObject;
        System.out.println(toString());
    }

    public RestServiceException(HttpStatus statusCode, String message, Throwable cause) {
        this(message);
        initCause(cause);
        setStatusCode(statusCode);
        System.out.println(toString());
    }

    public RestServiceException(HttpStatus statusCode, String message) {
        this(message);
        setStatusCode(statusCode);
        System.out.println(toString());
    }

    public RestServiceException(String message) {
        super(message == null || message.equals("") ? "Unknown error" : message);
    }

    private void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode == null ? HttpStatus.I_AM_A_TEAPOT : statusCode;
    }

    public static RestServiceException getInstance(Exception e) {
        Exception cause = (Exception) e.getCause();
        if (cause != null && cause instanceof RestServiceException)
            return (RestServiceException) cause;
        if (e instanceof RestServiceException)
            return (RestServiceException) e;

        if (e instanceof HttpMessageConversionException)
            return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not parse content", e);

        if (e instanceof ResourceAccessException)
            return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not access remote server", e);

        if (e instanceof RestClientException)
            return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown client error", e);

        return new RestServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public RestServiceErrorObject getErrorObject() {
        return errorObject;
    }

    @Override
    public String getMessage() {
        RestServiceErrorObject eo = getErrorObject();
        if (eo != null) {
            String message = eo.getMessage();
            if (!message.equals(""))
                return message;
        }
        return super.getMessage();
    }

    @Override
    public String toString() {
        String toString = statusCode.toString() + " " + statusCode.name() + " - " + super.getMessage();
        if (errorObject != null) {
            String errorObjectMessage = errorObject.getMessage();
            if (errorObjectMessage != null && !errorObjectMessage.equals(""))
                toString += " (" + errorObjectMessage + ")";
        }
        if (getCause() != null)
            toString += "\nCaused by: " + getCause().toString() + "";
        return toString;
    }
}
