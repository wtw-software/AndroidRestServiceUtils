package no.wtw.android.restserviceutils;

public abstract class SimpleRestCall implements RestServiceCallable<Void> {

    @Override
    final public Void executeInternal() throws Exception {
        execute();
        return null;
    }

    public abstract void execute() throws Exception;

}
