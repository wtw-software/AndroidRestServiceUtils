package no.wtw.android.restserviceutils.call;

import android.os.Build;
import android.util.Log;

import org.springframework.web.client.ResourceAccessException;

import java.io.EOFException;

import no.wtw.android.restserviceutils.BuildConfig;
import no.wtw.android.restserviceutils.exceptions.RestServiceException;

public abstract class RestCallRetrying<T> extends AbstractRestCall<T> {

    private static final String TAG = RestCallRetrying.class.getSimpleName();

    static final int MAX_CONNECTIONS = BuildConfig.DEBUG ? 0 : 5;

    private T recursiveRunner(int attemptNumber) throws Exception {
        if (attemptNumber++ < MAX_CONNECTIONS) {
            try {
                return run();
            } catch (ResourceAccessException | EOFException e) {
                Log.e(TAG, "Caught exception using Android API level " + Build.VERSION.SDK_INT + ". Retry number " + attemptNumber);
                Thread.sleep(500);
                return recursiveRunner(attemptNumber);
            } catch (RestServiceException e) {
                Log.e(TAG, e.getMessage());
                return run();
            }
        } else {
            return run();
        }
    }

    @Override
    public T executeInternal() throws Exception {
        return recursiveRunner(0);
    }

    public abstract T run() throws Exception;
}
