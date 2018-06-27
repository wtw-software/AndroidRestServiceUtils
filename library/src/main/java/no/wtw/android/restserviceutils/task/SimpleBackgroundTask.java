package no.wtw.android.restserviceutils.task;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public abstract class SimpleBackgroundTask<D> implements BackgroundTask<D> {

    @Override
    public void onLoadStart() {
    }

    @Override
    public abstract D onLoadExecute() throws Exception;

    @Override
    public void onLoadSuccess(D data) {
    }

    @Override
    public void onLoadError(Context context, Exception e) {
        if (context != null && e != null && !TextUtils.isEmpty(e.getMessage()))
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadEnd(Context context) {
    }

}