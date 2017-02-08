package no.wtw.android.restserviceutils.call;

public abstract class RestCallSimple extends AbstractRestCall<Void> {

    @Override
    public Void executeInternal() throws Exception {
        run();
        return null;
    }

    public abstract void run() throws Exception;

}
