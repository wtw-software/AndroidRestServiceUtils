package no.wtw.android.restserviceutils.resource

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import no.wtw.android.restserviceutils.exceptions.LinkNotResolvedException
import no.wtw.android.restserviceutils.exceptions.RestServiceException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.Serializable

class Link<T> : Serializable {

    companion object {
        const val LINK_SELF = "self"
    }

    @Transient
    private var _resource: T? = null
    val resource: T
        @Throws(LinkNotResolvedException::class)
        get() = _resource ?: throw LinkNotResolvedException()

    @Transient
    private var clazz: Class<T>? = null

    @SerializedName("key")
    var relation: String? = null
        protected set

    @SerializedName("href")
    var url: String? = null
        protected set

    var queryParams: Map<String, String>? = null

    constructor() {}
    constructor(relation: String?, url: String?) {
        this.relation = relation
        this.url = url
    }

    val isResolved: Boolean
        get() = _resource != null

    fun setClass(clazz: Class<T>?) {
        this.clazz = clazz
    }

    fun httpGet(client: OkHttpClient, gson: Gson): T {
        val qp = queryParams
        if (qp != null)
            return httpGet(client, gson, qp)
        return executeHttpCall {
            if (clazz == null)
                throw RuntimeException("Class of return object must be set")
            val request = Request.Builder().url(url!!).get().build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = gson.fromJson(body, clazz)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpGet(client: OkHttpClient, gson: Gson, queryParams: Map<String, String>): T {
        return executeHttpCall {
            if (clazz == null)
                throw RuntimeException("Class of return object must be set")
            var queryString = ""
            for (key in queryParams.keys)
                queryString += key + "=" + queryParams[key] + "&"
            val request = Request.Builder().url(url!! + "?" + queryString.substring(0, queryString.length - 1)).get().build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = gson.fromJson(body, clazz)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpGet(client: OkHttpClient, gson: Gson, query: JsonEncodedQuery): T {
        return executeHttpCall {
            if (clazz == null)
                throw RuntimeException("Class of return object must be set")
            val url = url!!.replace("?data=Base64", "") // TODO: remove this hack
            val request = Request.Builder().url(url + "?data=" + query.encode(true)).get().build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = gson.fromJson(body, clazz)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpGet(client: OkHttpClient, gson: Gson, urlAppendix: String): T {
        return executeHttpCall {
            if (clazz == null)
                throw RuntimeException("Class of return object must be set")
            val request = Request.Builder().url(url!! + urlAppendix).get().build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = gson.fromJson(body, clazz)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpPut(client: OkHttpClient, gson: Gson, data: T): T {
        return executeHttpCall {
            if (clazz == null)
                throw RuntimeException("Class of return object must be set")
            val jsonData = data?.let { gson.toJson(it) } ?: ""
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody: RequestBody = jsonData.toRequestBody(mediaType)
            val request = Request.Builder().url(url!!).put(requestBody).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                val body = response.body?.string() ?: ""
                _resource = gson.fromJson(body, clazz)
                _resource ?: throw NullPointerException("Body is null")
            }
        }
    }

    fun httpPost(client: OkHttpClient, gson: Gson, data: Any?): T {
        return executeHttpCall {
            val jsonData = data?.let { gson.toJson(it) } ?: ""
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody: RequestBody = jsonData.toRequestBody(mediaType)
            val request = Request.Builder().url(url!!).post(requestBody).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful)
                    throw RestServiceException.from(response)
                if (clazz == null) {
                    Unit as T
                } else {
                    val body = response.body?.string() ?: ""
                    gson.fromJson(body, clazz)
                }
            }
        }
    }

    fun httpDelete(client: OkHttpClient, gson: Gson): Unit =
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