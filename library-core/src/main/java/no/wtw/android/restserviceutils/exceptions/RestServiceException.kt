package no.wtw.android.restserviceutils.exceptions

import no.wtw.android.restserviceutils.GsonSingleton
import no.wtw.android.restserviceutils.HttpStatusCode
import no.wtw.android.restserviceutils.RestServiceErrorObject
import okhttp3.Response
import java.io.IOException

class RestServiceException(
    val statusCode: HttpStatusCode = HttpStatusCode.INTERNAL_SERVER_ERROR,
    message: String,
    cause: Throwable? = null
) : IOException(message) {

    init {
        cause?.let { initCause(it) }
    }

    companion object {

        const val ERROR_MESSAGE_HEADER = "x-wtw-errormessage"

        fun from(e: Exception): RestServiceException {
            val cause = e.cause as Exception?
            if (cause != null && cause is RestServiceException)
                return cause
            if (e is RestServiceException)
                return e
            return RestServiceException(HttpStatusCode.INTERNAL_SERVER_ERROR, e.message ?: "", cause = e)
        }

        fun from(response: Response): RestServiceException {
            val header = response.headers(ERROR_MESSAGE_HEADER).joinToString(separator = "\n")
            if (header.isNotBlank())
                return RestServiceException(HttpStatusCode.from(response.code), header)
            val body = response.body?.string()
                ?: return RestServiceException(HttpStatusCode.from(response.code), "Unknown error")
            if (body.startsWith("{")) {
                try {
                    val errorObject = GsonSingleton.getInstance().fromJson(body, RestServiceErrorObject::class.java)!!
                    return RestServiceException(HttpStatusCode.from(errorObject.code!!), errorObject.message!!)
                } catch (_: Exception) {
                }
            }
            return RestServiceException(HttpStatusCode.from(response.code), body)
        }

    }

    override fun toString(): String {
        var msg = "$statusCode - $message"
        cause?.let { msg += "\nCaused by: $it" }
        return msg
    }

}