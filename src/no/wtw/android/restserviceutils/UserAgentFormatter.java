package no.wtw.android.restserviceutils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class UserAgentFormatter {

    public static String getUserAgent(Context context) {
        return getAppName(context) + "/" + getVersion(context) + " " + getDevice() + " (" + getScreenSize(context) + ") Android/" + Build.VERSION.RELEASE;
    }

    private static String getAppName(Context context) {
        String name = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        Log.d("NAME: ", name);
        return name;
    }

    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName + " (" + info.versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            return "?";
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @SuppressWarnings("deprecation")
    public static String getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            return size.x + "x" + size.y;
        } else {
            return display.getWidth() + "x" + display.getHeight();
        }
    }

    public static String getDevice() {
        return Build.MODEL + "/" + Build.DEVICE;
    }
}
