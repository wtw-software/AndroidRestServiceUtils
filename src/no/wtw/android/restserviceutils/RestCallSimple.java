package no.wtw.android.restserviceutils;

public abstract class RestCallSimple extends AbstractRestCall<Void> {

    @Override
    final public Void executeInternal() throws Exception {
        run();
        return null;
    }

    public abstract void run() throws Exception;

}