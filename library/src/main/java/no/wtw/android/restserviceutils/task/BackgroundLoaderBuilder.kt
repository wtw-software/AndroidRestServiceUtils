package no.wtw.android.restserviceutils.task

class BackgroundLoaderBuilder<D>() {

    private lateinit var onExecute: (() -> D)
    private var onStart: (() -> Unit)? = null
    private var onSuccess: ((D) -> Unit)? = null
    private var onError: ((e: Exception) -> Unit)? = null
    private var onEnd: (() -> Unit)? = null

    fun onStart(block: (() -> Unit)) = apply { this.onStart = block }
    fun onExecute(block: (() -> D)) = apply { this.onExecute = block }
    fun onSuccess(block: ((D) -> Unit)) = apply { this.onSuccess = block }
    fun onError(block: ((e: Exception) -> Unit)) = apply { this.onError = block }
    fun onEnd(block: (() -> Unit)) = apply { this.onEnd = block }

    fun build(): BackgroundLoader<D> {
        return BackgroundLoader(object : BackgroundTask<D> {
            override fun onLoadStart() {
                onStart?.invoke()
            }

            override fun onLoadExecute(): D {
                return onExecute.invoke()
            }

            override fun onLoadSuccess(data: D) {
                onSuccess?.invoke(data)
            }

            override fun onLoadError(e: Exception) {
                onError?.invoke(e)
            }

            override fun onLoadEnd() {
                onEnd?.invoke()
            }
        })
    }

    fun start() = build().start()

}

fun <D> doInBackground() = BackgroundLoaderBuilder<D>()