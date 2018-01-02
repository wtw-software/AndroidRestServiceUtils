package no.wtw.android.restserviceutils;

import no.wtw.android.restserviceutils.exceptions.RestServiceException;

public interface RestServiceCredentialProvider {

    public String getPassword() throws RestServiceException;

    public String getUsername() throws RestServiceException;
}
