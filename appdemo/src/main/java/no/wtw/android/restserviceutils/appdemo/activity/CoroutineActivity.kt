package no.wtw.android.restserviceutils.appdemo.activity

import android.app.Activity
import android.content.Context
import android.util.Log
import no.wtw.android.restserviceutils.task.BackgroundLoader
import no.wtw.android.restserviceutils.task.BackgroundTask

class CoroutineActivity : Activity() {

    private lateinit var loader: BackgroundLoader<String>

    override fun onResume() {
        super.onResume()

        loader = BackgroundLoader(this, object : BackgroundTask<String> {
            override fun onLoadStart() {
                Log.e("CR", "start " + Thread.currentThread().name)
            }

            override fun onLoadExecute(): String {
                Log.e("CR", "executing.." + Thread.currentThread().name)
                Thread.sleep(3000)
                // throw RuntimeException("runtimeexception")
                Log.e("CR", "done executing " + Thread.currentThread().name)
                return "success!"
            }

            override fun onLoadSuccess(data: String) {
                Log.e("CR", "success " + data + " " + Thread.currentThread().name)
            }

            override fun onLoadError(context: Context?, e: Exception?) {
                Log.e("CR", "error " + (e?.message ?: "?") + " " + Thread.currentThread().name)
            }

            override fun onLoadEnd(context: Context?) {
                Log.e("CR", "end " + Thread.currentThread().name)
            }
        })
        loader.start()
    }

}