package no.wtw.android.restserviceutils.task

interface BackgroundTask<D> {
    fun onLoadStart()

    @Throws(Exception::class)
    fun onLoadExecute(): D

    fun onLoadSuccess(data: D)

    fun onLoadError(e: Exception)

    fun onLoadEnd()

}