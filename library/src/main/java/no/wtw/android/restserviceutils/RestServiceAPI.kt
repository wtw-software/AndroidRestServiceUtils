package no.wtw.android.restserviceutils

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import no.wtw.android.restserviceutils.exceptions.RestServiceException
import no.wtw.android.restserviceutils.resource.JsonEncodedQuery
import no.wtw.android.restserviceutils.resource.Link
import okhttp3.OkHttpClient
import java.util.*

abstract class RestServiceAPI {

    /**
     * Convenience method that throws an exception if network connection is not available.
     * May be overridden by sub class to provide better information on what to do in offline cases.
     *
     * @throws RestServiceException
     */
    @Throws(RestServiceException::class)
    fun assertIsOnline() {
        if (!isOnline)
            throw RestServiceException(HttpStatusCode.SERVICE_UNAVAILABLE, "Network unavailable")
    }

    val isOnline: Boolean
        get() {
            val cm = getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = cm.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isAvailable && activeNetworkInfo.isConnected
        }

    abstract fun getContext(): Context
    abstract fun getClient(): OkHttpClient
    abstract fun getGson(): Gson
    open fun getConnectTimeout() = 60000
    open fun getReadTimeout() = 60000

}

fun <T> Link<T>.httpGet(api: RestServiceAPI): T = this.httpGet(api.getClient(), api.getGson())
fun <T> Link<T>.httpGet(api: RestServiceAPI, queryParams: Map<String, String>): T = this.httpGet(api.getClient(), api.getGson(), queryParams)
fun <T> Link<T>.httpGet(api: RestServiceAPI, query: JsonEncodedQuery): T = this.httpGet(api.getClient(), api.getGson(), query)
fun <T> Link<T>.httpDelete(api: RestServiceAPI): Unit = this.httpDelete(api.getClient(), api.getGson())
fun <T> Link<T>.httpPut(api: RestServiceAPI, body: T): T = this.httpPut(api.getClient(), api.getGson(), body)
fun <T> Link<T>.httpPost(api: RestServiceAPI, body: Any?): T = this.httpPost(api.getClient(), api.getGson(), body)
