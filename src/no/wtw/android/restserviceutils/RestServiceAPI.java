package no.wtw.android.restserviceutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public abstract class RestServiceAPI {

    private static final String TAG = RestServiceAPI.class.getSimpleName();

    public void setDefaultInterceptor() {
        ClientHttpRequestInterceptor i = new RestServiceRequestInterceptor(this);
        getRestTemplate().setInterceptors(Arrays.asList(i));
    }

    public void setDefaultErrorHandler() {
        getRestTemplate().setErrorHandler(new RestServiceErrorHandler());
    }

    public boolean isOnline() throws RestServiceException {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * Convenience method that throws an exception if network connection is not available.
     * May be overridden by sub class to provide better information on what to do in offline cases.
     *
     * @throws no.wtw.android.restserviceutils.RestServiceException
     */
    protected void checkNetwork() throws RestServiceException {
        if (!isOnline())
            throw new RestServiceException(HttpStatus.SERVICE_UNAVAILABLE, "Network unavailable");
    }

    public abstract RestTemplate getRestTemplate();

    public abstract Context getContext();

    public abstract void setAuthentication();

    protected <T> T call(RestCall<T> restCall) throws RestServiceException {
        try {
            checkNetwork();
            setAuthentication();
            return restCall.execute();
        } catch (Exception e) {
            if (e.getMessage() != null)
                Log.e(TAG, e.getMessage());
            e.printStackTrace();
            throw RestServiceException.getInstance(e);
        }
    }

}
