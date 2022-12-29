package no.wtw.android.restserviceutils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import no.wtw.android.restserviceutils.RestServiceAPI
import no.wtw.android.restserviceutils.exceptions.RestServiceException
import no.wtw.android.restserviceutils.resource.JsonEncodedQuery
import no.wtw.android.restserviceutils.resource.Link
import org.springframework.http.*
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate
import java.util.*

abstract class RestServiceAPI<S> {

    fun setDefaultRequestFactory() {
        restTemplate.requestFactory = CustomRequestFactory(this)
    }

    fun setDefaultInterceptor() {
        val i: ClientHttpRequestInterceptor = RestServiceRequestInterceptor(this)
        restTemplate.interceptors = Arrays.asList(i)
    }

    fun setDefaultErrorHandler() {
        restTemplate.errorHandler = RestServiceErrorHandler()
    }

    val isOnline: Boolean
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = cm.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isAvailable && activeNetworkInfo.isConnected
        }

    /**
     * Convenience method that throws an exception if network connection is not available.
     * May be overridden by sub class to provide better information on what to do in offline cases.
     *
     * @throws RestServiceException
     */
    @Throws(RestServiceException::class)
    fun checkNetwork() {
        if (!isOnline) throw RestServiceException(HttpStatus.SERVICE_UNAVAILABLE, "Network unavailable")
    }

    abstract val restTemplate: RestTemplate
    abstract val context: Context

    val authentication: HttpAuthentication
        get() = HttpBasicAuthentication("", "")

    @Throws(RestServiceException::class)
    fun <T, C : Call<S, T>?> call(call: C): T {
        return try {
            checkNetwork()
            call!!.execute(service)
        } catch (e: Exception) {
            if (e.message != null) Log.e(TAG, e.message!!)
            throw RestServiceException.getInstance(e)
        }
    }

    @Throws(RestServiceException::class)
    fun <C : VoidCall<S>?> callVoid(call: C) {
        try {
            checkNetwork()
            call!!.execute(service)
        } catch (e: Exception) {
            if (e.message != null) Log.e(TAG, e.message!!)
            throw RestServiceException.getInstance(e)
        }
    }

    protected abstract val service: S
    val connectTimeout: Int
        get() = 60000
    val readTimeout: Int
        get() = 60000

    interface Call<S, T> {
        fun execute(service: S): T
    }

    interface VoidCall<S> {
        fun execute(service: S)
    }

    companion object {
        private val TAG = RestServiceAPI::class.java.simpleName
    }

    abstract fun <T> httpGet(link: Link<T>): T
    abstract fun <T> httpGet(link: Link<T>, query: JsonEncodedQuery): T
    abstract fun <T> httpGet(link: Link<T>, queryParams: Map<String, String>): T
    abstract fun <T> httpGet(link: Link<T>, urlAppendix: String): T
    abstract fun <T> httpPut(link: Link<T>, body: T): T
    abstract fun <T> httpPost(link: Link<T>, body: Any?): T
    abstract fun <T> httpDelete(link: Link<T>)

}

fun <T> Link<T>.httpGet(api: RestServiceAPI<*>): T = api.httpGet(this)
fun <T> Link<T>.httpGet(api: RestServiceAPI<*>, queryParams: Map<String, String>): T = api.httpGet(this, queryParams)
fun <T> Link<T>.httpGet(api: RestServiceAPI<*>, query: JsonEncodedQuery): T = api.httpGet(this, query)
fun <T> Link<T>.httpDelete(api: RestServiceAPI<*>): Unit = api.httpDelete(this)
fun <T> Link<T>.httpPut(api: RestServiceAPI<*>, body: T): T = api.httpPut(this, body)
fun <T> Link<T>.httpPost(api: RestServiceAPI<*>, body: Any?): T = api.httpPost(this, body)
