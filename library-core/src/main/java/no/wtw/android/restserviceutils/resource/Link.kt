package no.wtw.android.restserviceutils.resource

import com.google.gson.annotations.SerializedName
import no.wtw.android.restserviceutils.exceptions.LinkNotResolvedException
import no.wtw.android.restserviceutils.exceptions.RestServiceException
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