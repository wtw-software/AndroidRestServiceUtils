package no.wtw.android.restserviceutils.task;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class BackgroundLoader<D> {

    private final BackgroundTask<D> callback;
    private Handler handler_ = new Handler(Looper.getMainLooper());
    private Context context;

    public BackgroundLoader(Context context, BackgroundTask<D> callback) {
        super();
        this.context = context;
        this.callback = callback;
    }

    public void start() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {
            @Override
            public void execute() {
                try {
                    load();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    protected void load() {
        handler_.post(callback::onLoadStart);
        try {
            final D data = callback.onLoadExecute();
            handler_.post(() -> callback.onLoadSuccess(data));
        } catch (final Exception e) {
            handler_.post(() -> callback.onLoadError(context, e));
        }
        handler_.post(() -> callback.onLoadEnd(context));
    }

}