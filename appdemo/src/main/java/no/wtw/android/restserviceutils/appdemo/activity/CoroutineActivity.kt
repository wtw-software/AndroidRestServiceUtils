package no.wtw.android.restserviceutils.appdemo.activity

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import no.wtw.android.restserviceutils.appdemo.R
import no.wtw.android.restserviceutils.task.BackgroundLoader
import no.wtw.android.restserviceutils.task.BackgroundTask
import no.wtw.android.restserviceutils.task.doInBackground

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

        doInBackground<String>()
            .onStart {
                log("onLoadStart()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadStart()")
            }
            .onExecute {
                log("onLoadExecute()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                if (Math.random() > 0.5)
                    throw RuntimeException("RunTimeException")
                log("onLoadExecute()")
                ""
            }
            .onSuccess {
                log("onLoadSuccess()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadSuccess()")
            }
            .onError { e ->
                log("onLoadError() " + e.message)
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadError()")
            }
            .onEnd {
                log("onLoadEnd()")
                Thread.sleep((Math.random() * 3000.0).toLong())
                log("onLoadEnd()")

            }
            .start()
    }

    fun log(msg: String) {
        logView.append(msg + " " + Thread.currentThread().name + "\n")
    }

}