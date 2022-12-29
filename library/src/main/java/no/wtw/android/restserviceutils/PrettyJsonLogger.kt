package no.wtw.android.restserviceutils

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

class PrettyJsonLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "ApiLogger"
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val parse = JsonParser.parseString(message)
                val prettyPrintJson = GsonBuilder().setPrettyPrinting().create().toJson(parse)
                Log.v(logName, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Log.d(logName, message)
            }
        } else if (message.startsWith("<--") || message.startsWith("-->")) {
            Log.i(logName, message)
        } else {
            Log.d(logName, message)
        }
    }
}
