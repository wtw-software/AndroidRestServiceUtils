package no.wtw.android.restserviceutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.DisplayMetrics;

public class UserAgentFormatter {

    public static String getUserAgent(Context context) {
        return getAppName(context) + "/" + getVersion(context) + " " + getDevice() + " (" + getScreenSize(context) + ") Android/" + Build.VERSION.RELEASE;
    }

    private static String getAppName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
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

    public static String getScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels + "x" + metrics.heightPixels + "@" + metrics.density;
    }

    public static String getDevice() {
        return Build.MODEL + "/" + Build.DEVICE;
    }
}
