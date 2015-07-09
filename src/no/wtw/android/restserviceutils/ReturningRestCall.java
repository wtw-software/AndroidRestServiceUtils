package no.wtw.android.restserviceutils;

public abstract class ReturningRestCall<T> implements RestServiceCallable<T> {

    @Override
    final public T executeInternal() throws Exception {
        return execute();
    }

    public abstract T execute() throws Exception;

}
