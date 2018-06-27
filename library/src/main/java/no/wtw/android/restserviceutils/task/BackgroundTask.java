package no.wtw.android.restserviceutils.task;

import android.content.Context;

public interface BackgroundTask<D> {

    void onLoadStart();

    D onLoadExecute() throws Exception;

    void onLoadSuccess(D data);

    void onLoadError(Context context, Exception e);

    void onLoadEnd(Context context);

}