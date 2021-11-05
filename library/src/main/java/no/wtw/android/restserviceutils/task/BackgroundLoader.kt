package no.wtw.android.restserviceutils.task

import kotlinx.coroutines.*

open class BackgroundLoader<D>(private val task: BackgroundTask<D>) {

    private val scope = CoroutineScope(Dispatchers.Main)

    fun start() {
        scope.launch {
            load()
        }
    }

    private suspend fun load() {
        task.onLoadStart()
        try {
            val data = withContext(Dispatchers.IO) {
                task.onLoadExecute()
            }
            task.onLoadSuccess(data)
        } catch (e: Exception) {
            if (scope.isActive)
                task.onLoadError(e)
        }
        if (scope.isActive)
            task.onLoadEnd()
    }

    fun cancel() = scope.cancel()

}