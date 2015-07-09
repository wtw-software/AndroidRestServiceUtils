package no.wtw.android.restserviceutils;

interface RestServiceCallable<T> {

    public abstract T executeInternal() throws Exception;

}
