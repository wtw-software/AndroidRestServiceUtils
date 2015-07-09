package no.wtw.android.restserviceutils;

public interface RestCall<T> {

    T execute() throws Exception;

}
