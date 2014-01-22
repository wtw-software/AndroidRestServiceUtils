package no.wtw.android.restserviceutils;

import android.util.Log;
import org.springframework.http.HttpBasicAuthentication;

public class RestServiceAuthentication extends HttpBasicAuthentication {

    public RestServiceAuthentication(RestServiceCredentialProvider provider) throws RestServiceException {
        super(provider.getUsername(), provider.getPassword());
        Log.d("AUTH", toString());
    }

}
