package no.wtw.android.restserviceutils;

public abstract class RestCall<T> extends AbstractRestCall<T> {

    @Override
    public T executeInternal() throws Exception {
        return run();
    }

    public abstract T run() throws Exception;

}
