package no.wtw.android.restserviceutils;

public interface RestServiceCredentialProvider {

    public String getPassword() throws RestServiceException;

    public String getUsername() throws RestServiceException;
}
