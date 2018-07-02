package no.wtw.android.restserviceutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import no.wtw.android.restserviceutils.exceptions.RestServiceException;

public abstract class RestServiceAPI<S> {

    private static final String TAG = RestServiceAPI.class.getSimpleName();

    public void setDefaultInterceptor() {
        ClientHttpRequestInterceptor i = new RestServiceRequestInterceptor(this);
        getRestTemplate().setInterceptors(Arrays.asList(i));
    }

    public void setDefaultErrorHandler() {
        getRestTemplate().setErrorHandler(new RestServiceErrorHandler());
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    /**
     * Convenience method that throws an exception if network connection is not available.
     * May be overridden by sub class to provide better information on what to do in offline cases.
     *
     * @throws RestServiceException
     */
    public void checkNetwork() throws RestServiceException {
        if (!isOnline())
            throw new RestServiceException(HttpStatus.SERVICE_UNAVAILABLE, "Network unavailable");
    }

    public abstract RestTemplate getRestTemplate();

    public abstract Context getContext();

    public HttpAuthentication getAuthentication() {
        return new HttpBasicAuthentication("", "");
    }

    public <T, C extends Call<S, T>> T call(C call) throws RestServiceException {
        try {
            checkNetwork();
            return call.execute(getService());
        } catch (Exception e) {
            if (e.getMessage() != null)
                Log.e(TAG, e.getMessage());
            throw RestServiceException.getInstance(e);
        }
    }

    protected abstract S getService();

    public interface Call<S, T> {
        T execute(S service);
    }

}
