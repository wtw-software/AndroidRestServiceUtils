package no.wtw.android.restserviceutils.task

import android.content.Context
import android.widget.Toast

abstract class SimpleBackgroundTask<D> : BackgroundTask<D> {
    override fun onLoadStart() {}

    @Throws(Exception::class)
    abstract override fun onLoadExecute(): D

    override fun onLoadSuccess(data: D) {}

    override fun onLoadError(context: Context?, e: Exception) {
        if (context != null)
            Toast.makeText(context, e.message ?: "Unknown error", Toast.LENGTH_SHORT).show()
    }

    override fun onLoadEnd(context: Context?) {}

}