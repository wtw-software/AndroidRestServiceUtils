package no.wtw.android.restserviceutils

import com.google.gson.annotations.SerializedName

data class RestServiceErrorObject(
    @SerializedName("code")
    var code: Int?,
    @SerializedName("subCode")
    val subCode: Int = -1,
    @SerializedName("message")
    var message: String?,
) {

    override fun toString() = "$code/$code $message"

}