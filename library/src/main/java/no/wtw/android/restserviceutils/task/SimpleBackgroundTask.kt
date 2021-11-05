package no.wtw.android.restserviceutils.task

abstract class SimpleBackgroundTask<D> : BackgroundTask<D> {
    override fun onLoadStart() {}

    @Throws(Exception::class)
    abstract override fun onLoadExecute(): D

    override fun onLoadSuccess(data: D) {}

    override fun onLoadError(e: Exception) {}

    override fun onLoadEnd() {}

}