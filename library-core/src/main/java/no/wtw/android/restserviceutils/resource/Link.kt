package no.wtw.android.restserviceutils.resource

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import no.wtw.android.restserviceutils.exceptions.LinkNotResolvedException
import no.wtw.android.restserviceutils.exceptions.RestServiceException
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import java.io.Serializable
import java.net.URI

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

    fun httpGet(client: OkHttpClient, gson: Gson): T =
        executeHttpCall {
            val request = Request.Builder().url(url ?: throw RestServiceException("URL is null")).get().build()
            client.newCall(request).execute().use { response ->
                val body = response.body?.string() ?: ""
                response.body?.close()
                if (!response.isSuccessful)
                    throw RestServiceException(HttpStatus.valueOf(response.code), body)
                _resource = gson.fromJson(body, clazz)
                _resource!!
            }
        }

    fun httpGet(restTemplate: RestTemplate): T {
        if (clazz == null)
            throw RuntimeException("Class of return object must be set")
        return if (queryParams != null)
            httpGet(restTemplate, queryParams)
        else
            executeHttpCall {
                _resource = restTemplate.getForObject(url, clazz)
                _resource ?: throw RestServiceException("Body is null")
            }
    }

    fun httpGet(restTemplate: RestTemplate, query: JsonEncodedQuery): T {
        if (clazz == null)
            throw RuntimeException("Class of return object must be set")
        return executeHttpCall {
            val url = url!!.replace("?data=Base64", "") // TODO: remove this hack
            _resource = restTemplate.getForObject(url + "?data=" + query.encode(true), clazz)
            _resource ?: throw RestServiceException("Body is null")
        }
    }

    fun httpGet(restTemplate: RestTemplate, queryParams: Map<String, String>?): T {
        if (clazz == null) throw RuntimeException("Class of return object must be set")
        return executeHttpCall {
            var queryString = ""
            for (key in queryParams!!.keys) queryString += key + "=" + queryParams[key] + "&"
            _resource = restTemplate.getForObject(url + "?" + queryString.substring(0, queryString.length - 1), clazz)
            _resource ?: throw RestServiceException("Body is null")
        }
    }

    fun httpGet(restTemplate: RestTemplate, urlAppendix: String): T {
        if (clazz == null)
            throw RuntimeException("Class of return object must be set")
        return executeHttpCall {
            _resource = restTemplate.getForObject(url + urlAppendix, clazz)
            _resource ?: throw RestServiceException("Body is null")
        }
    }

    fun httpPut(restTemplate: RestTemplate, body: T): T =
        executeHttpCall {
            val requestEntity = HttpEntity(body, HttpHeaders())
            val responseEntity = restTemplate.exchange(URI.create(url), HttpMethod.PUT, requestEntity, clazz)
            if (HttpStatus.OK == responseEntity.statusCode)
                _resource = responseEntity.body
            _resource ?: throw RestServiceException("Body is null")
        }

    fun httpPost(restTemplate: RestTemplate, body: Any?): T =
        executeHttpCall {
            restTemplate.postForEntity(url, body, clazz, HashMap<String, Any?>()).body
        }

    fun httpDelete(restTemplate: RestTemplate) =
        executeHttpCall {
            restTemplate.delete(url)
        }

    val isResolved: Boolean
        get() = _resource != null

    fun setClass(clazz: Class<T>?) {
        this.clazz = clazz
    }

    private fun <RT> executeHttpCall(block: () -> RT): RT {
        return try {
            block()
        } catch (e: Exception) {
            if (e.message != null)
                println(e.message)
            throw RestServiceException.getInstance(e)
        }
    }

}