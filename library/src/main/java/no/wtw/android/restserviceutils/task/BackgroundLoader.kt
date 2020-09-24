package no.wtw.android.restserviceutils.task

import android.content.Context
import kotlinx.coroutines.*

open class BackgroundLoader<D>(private val context: Context, private val task: BackgroundTask<D>) {

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
                task.onLoadError(context, e)
        }
        if (scope.isActive)
            task.onLoadEnd(context)
    }

    fun cancel() = scope.cancel()

}