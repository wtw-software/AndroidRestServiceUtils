package no.wtw.android.restserviceutils;

import org.springframework.http.HttpBasicAuthentication;

import no.wtw.android.restserviceutils.exceptions.RestServiceException;

public class RestServiceAuthentication extends HttpBasicAuthentication {

    public RestServiceAuthentication(RestServiceCredentialProvider provider) throws RestServiceException {
        super(provider.getUsername(), provider.getPassword());
    }

}
