package no.wtw.android.restserviceutils.call;

import android.util.Log;

import no.wtw.android.restserviceutils.RestServiceAPI;
import no.wtw.android.restserviceutils.exceptions.RestServiceException;

public class RestCallBuilder {

    private static final String TAG = RestCallBuilder.class.getSimpleName();

    private RestServiceAPI restServiceAPI;
    private boolean isNetworkCheck = true;

    private RestCallBuilder() {
    }

    public static RestCallBuilder with(RestServiceAPI restServiceAPI) {
        RestCallBuilder restCallBuilder = new RestCallBuilder();
        restCallBuilder.restServiceAPI = restServiceAPI;
        return restCallBuilder;
    }

    public RestCallBuilder online(boolean shouldCheckNeworkConnection) {
        this.isNetworkCheck = shouldCheckNeworkConnection;
        return this;
    }

    public <T, C extends AbstractRestCall<T>> T call(C call) throws RestServiceException {
        try {
            if (isNetworkCheck)
                restServiceAPI.checkNetwork();
            return call.executeInternal();
        } catch (Exception e) {
            if (e.getMessage() != null)
                Log.e(TAG, e.getMessage());
            e.printStackTrace();
            throw RestServiceException.getInstance(e);
        }
    }

}
