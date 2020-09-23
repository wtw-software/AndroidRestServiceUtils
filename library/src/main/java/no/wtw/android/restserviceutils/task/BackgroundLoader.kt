package no.wtw.android.restserviceutils.task

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BackgroundLoader<D>(private val context: Context, private val callback: BackgroundTask<D>) {

    fun start() {
        CoroutineScope(Dispatchers.Main).launch {
            callback.onLoadStart()
            load()
            callback.onLoadEnd(context)
        }
    }

    private suspend fun load() {
        try {
            val data: D = withContext(Dispatchers.IO) {
                callback.onLoadExecute()
            }
            callback.onLoadSuccess(data)
        } catch (e: Exception) {
            callback.onLoadError(context, e)
        }
    }

}