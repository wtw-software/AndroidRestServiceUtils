package no.wtw.android.restserviceutils.appdemo.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import no.wtw.android.restserviceutils.appdemo.R
import no.wtw.android.restserviceutils.task.BackgroundLoader
import no.wtw.android.restserviceutils.task.BackgroundTask

class CoroutineActivity : Activity() {

    private lateinit var logView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coroutine_activity)
        logView = findViewById(R.id.log)
    }

    override fun onResume() {
        super.onResume()
        log("")

        val loader = BackgroundLoader(this, object : BackgroundTask<String> {
            override fun onLoadStart() {
                log("onLoadStart()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadStart()")
            }

            override fun onLoadExecute(): String {
                log("onLoadExecute()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                if (Math.random() > 0.5)
                    throw RuntimeException("RunTimeException")
                log("onLoadExecute()")
                return ""
            }

            override fun onLoadSuccess(data: String) {
                log("onLoadSuccess()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadSuccess()")
            }

            override fun onLoadError(context: Context, e: Exception) {
                log("onLoadError() " + e.message)
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadError()")
            }

            override fun onLoadEnd(context: Context) {
                log("onLoadEnd()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadEnd()")
            }
        }).start()
    }

    fun log(msg: String) {
        logView.append(msg + " " + Thread.currentThread().name + "\n")
    }

}