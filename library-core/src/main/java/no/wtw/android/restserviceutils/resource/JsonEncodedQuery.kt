package no.wtw.android.restserviceutils.resource

import no.wtw.android.restserviceutils.GsonSingleton
import java.io.Serializable

abstract class JsonEncodedQuery : Serializable {

    override fun toString(): String {
        return encode(true)
    }

    fun encode(isBase64Encoded: Boolean): String {
        val json = GsonSingleton.getInstance().toJson(this)
        println(json)
        return if (isBase64Encoded)
            TODO("Base64 implementation needed") // Base64.encodeBytes(json.toByteArray())
        else
            json
    }

}