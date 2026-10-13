package no.wtw.android.restserviceutils.resource

import com.google.gson.annotations.SerializedName
import no.wtw.android.restserviceutils.RestServiceJsonParser
import no.wtw.android.restserviceutils.exceptions.LinkNotResolvedException
import no.wtw.android.restserviceutils.exceptions.RestServiceException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.Serializable
import kotlin.reflect.KClass

class Link<T : Any> : Serializable {

    companion object {
        const val LINK_SELF = "self"
    }

    @Transient
    private var _resource: T? = null
    val resource: T
        @Throws(LinkNotResolvedException::class)
        get() = _resource ?: throw LinkNotResolvedException()

    @Transient
    var kclass: KClass<T>? = null

    @SerializedName("key")
    var relation: String? = null
        protected set

    @SerializedName("href")
    var url: String? = null
        protected set

    constructor() {}
    constructor(relation: String?, url: String?) {
        this.relation = relation
        this.url = url
    }

    val isResolved: Boolean
        get() = _resource != null

    fun setClass(kclass: KClass<T>?) {
        this.kclass = kclass
    }

    fun httpGet(client: OkHttpClient, jsonParser: RestServiceJsonParser, queryParams: Map<String, String> = emptyMap()): T {
        return executeHttpCall {
            val kc = kclass
            checkNotNull(kc) { "Class of return object must be set" }
            var urlAndParams = url
            if (queryParams.isNotEmpty())
                urlAndParams += "?" + queryParams.map { "${it.key}=${it.value}" }.joinToString(separator = "&")
            check(!urlAndParams.isNullOrEmpty()) { "URL can not be null or empty" }
            val request = Request.Builder().url(urlAndParams).get().build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = jsonParser.fromJson(body, kc)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpGet(client: OkHttpClient, jsonParser: RestServiceJsonParser, urlAppendix: String): T {
        return executeHttpCall {
            val kc = kclass
            checkNotNull(kc) { "Class of return object must be set" }
            val request = Request.Builder().url(url!! + urlAppendix).get().build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = jsonParser.fromJson(body, kc)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpPut(client: OkHttpClient, jsonParser: RestServiceJsonParser, data: T): T {
        return executeHttpCall {
            val kc = kclass
            checkNotNull(kc) { "Class of return object must be set" }
            val jsonData = data?.let { jsonParser.toJson(it) } ?: ""
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody: RequestBody = jsonData.toRequestBody(mediaType)
            val request = Request.Builder().url(url!!).put(requestBody).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = jsonParser.fromJson(body, kc)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpPost(client: OkHttpClient, jsonParser: RestServiceJsonParser, data: Any?): T {
        return executeHttpCall {
            val jsonData = data?.let { jsonParser.toJson(it) } ?: ""
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody: RequestBody = jsonData.toRequestBody(mediaType)
            val request = Request.Builder().url(url!!).post(requestBody).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                if (kclass == null) {
                    Unit as T
                } else {
                    val kc = kclass
                    checkNotNull(kc) { "Class of return object must be set" }
                    val body = response.body?.string() ?: ""
                    jsonParser.fromJson(body, kc)
                }
            }
        }
    }

    fun httpDelete(client: OkHttpClient): Unit =
        executeHttpCall {
            val request = Request.Builder().url(url!!).delete().build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
            }
        }

    private fun <RT> executeHttpCall(block: () -> RT): RT {
        return try {
            block()
        } catch (e: Exception) {
            if (e.message != null)
                println(e.message)
            throw RestServiceException.from(e)
        }
    }

}