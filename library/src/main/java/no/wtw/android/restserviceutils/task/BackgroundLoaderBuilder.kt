package no.wtw.android.restserviceutils.task

import android.content.Context

class BackgroundLoaderBuilder<D>(private val ctx: Context) {

    private lateinit var onExecute: (() -> D)
    private var onStart: (() -> Unit)? = null
    private var onSuccess: ((D) -> Unit)? = null
    private var onError: ((ctx: Context, e: Exception) -> Unit)? = null
    private var onEnd: (() -> Unit)? = null

    fun onStart(block: (() -> Unit)) = apply { this.onStart = block }
    fun onExecute(block: (() -> D)) = apply { this.onExecute = block }
    fun onSuccess(block: ((D) -> Unit)) = apply { this.onSuccess = block }
    fun onError(block: ((ctx: Context, e: Exception) -> Unit)) = apply { this.onError = block }
    fun onEnd(block: (() -> Unit)) = apply { this.onEnd = block }

    fun build(): BackgroundLoader<D> {
        return BackgroundLoader(ctx, object : BackgroundTask<D> {
            override fun onLoadStart() {
                onStart?.invoke()
            }

            override fun onLoadExecute(): D {
                return onExecute.invoke()
            }

            override fun onLoadSuccess(data: D) {
                onSuccess?.invoke(data)
            }

            override fun onLoadError(context: Context, e: Exception) {
                onError?.invoke(ctx, e)
            }

            override fun onLoadEnd(context: Context) {
                onEnd?.invoke()
            }
        })
    }

    fun start() = build().start()

}

fun <D> doInBackground(ctx: Context) = BackgroundLoaderBuilder<D>(ctx)