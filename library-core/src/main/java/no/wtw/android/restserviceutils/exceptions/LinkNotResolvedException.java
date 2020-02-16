package no.wtw.android.restserviceutils.exceptions;

public class LinkNotResolvedException extends Exception {

    public LinkNotResolvedException() {
        super("Linked resource has not been resolved yet.");
    }

}
