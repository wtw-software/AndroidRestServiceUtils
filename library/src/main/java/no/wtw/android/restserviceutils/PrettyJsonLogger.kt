package no.wtw.android.restserviceutils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

class PrettyJsonLogger : HttpLoggingInterceptor.Logger {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val tag = "PrettyJsonLogger"

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val parse = JsonParser.parseString(message)
                Log.v(tag, gson.toJson(parse))
            } catch (m: JsonSyntaxException) {
                Log.d(tag, message)
            }
        } else if (message.startsWith("<--") || message.startsWith("-->")) {
            Log.i(tag, message)
        } else {
            Log.d(tag, message)
        }
    }
}
