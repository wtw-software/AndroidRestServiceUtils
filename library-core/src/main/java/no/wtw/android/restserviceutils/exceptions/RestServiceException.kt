package no.wtw.android.restserviceutils.exceptions

import no.wtw.android.restserviceutils.HttpStatusCode
import no.wtw.android.restserviceutils.RestServiceErrorObject
import okhttp3.Response
import java.io.IOException

class RestServiceException(
    val statusCode: HttpStatusCode = HttpStatusCode.INTERNAL_SERVER_ERROR,
    message: String,
    val errorObject: RestServiceErrorObject? = null,
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
            var messages = response.headers(ERROR_MESSAGE_HEADER).joinToString(separator = "\n")
            if (messages.isBlank())
                messages = response.body?.string() ?: "Unknown error"
            return RestServiceException(HttpStatusCode.from(response.code), messages)
        }
    }

    override val message: String
        get() = errorObject?.message?.takeIf { it.isNotBlank() } ?: super.message ?: ""

    override fun toString(): String {
        var msg = statusCode.toString() + " " + statusCode.name + " - " + super.message
        errorObject?.message?.takeIf { it.isNotBlank() }?.let { msg += " ($it)" }
        cause?.let { msg += "\nCaused by: $it" }
        return msg
    }

}