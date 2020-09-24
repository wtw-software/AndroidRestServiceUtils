package no.wtw.android.restserviceutils.task

import android.content.Context

interface BackgroundTask<D> {
    fun onLoadStart()

    @Throws(Exception::class)
    fun onLoadExecute(): D

    fun onLoadSuccess(data: D)

    fun onLoadError(context: Context, e: Exception)

    fun onLoadEnd(context: Context)

}