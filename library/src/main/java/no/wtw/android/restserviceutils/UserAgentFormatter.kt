package no.wtw.android.restserviceutils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

open class UserAgentFormatter(
    private val context: Context,
) {

    val userAgent: String
        get() = appName + "/" + version + " " + device + " (" + screenSize + ") Android/" + Build.VERSION.RELEASE

    val appName: String
        get() = context.applicationInfo.loadLabel(context.packageManager).toString()

    val version: String
        get() = try {
            context.packageManager.getPackageInfo(context.packageName, 0).let { it.versionName + " (" + it.versionCode + ")" }
        } catch (e: PackageManager.NameNotFoundException) {
            "?"
        }

    val screenSize: String
        get() = context.resources.displayMetrics.let { it.widthPixels.toString() + "x" + it.heightPixels + "@" + it.density }

    val device: String
        get() = Build.MODEL + "/" + Build.DEVICE

}