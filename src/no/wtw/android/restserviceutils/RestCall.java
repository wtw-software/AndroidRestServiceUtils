package no.wtw.android.restserviceutils;

public abstract class RestCall<T> extends AbstractRestCall<T> {

    @Override
    final public T executeInternal() throws Exception {
        return run();
    }

    public abstract T run() throws Exception;

}
