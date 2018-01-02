package no.wtw.android.restserviceutils.call;

public abstract class AbstractRestCall<T> {

    protected AbstractRestCall() {
    }

    public abstract T executeInternal() throws Exception;

}
