package no.wtw.android.restserviceutils;

abstract class AbstractRestCall<T> {

    protected AbstractRestCall() {
    }

    public abstract T executeInternal() throws Exception;

}
