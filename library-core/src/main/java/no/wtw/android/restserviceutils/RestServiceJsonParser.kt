package no.wtw.android.restserviceutils

import kotlin.reflect.KClass

interface RestServiceJsonParser {

    fun toJson(data: Any?): String
    fun <T : Any> fromJson(json: String, classOfT: KClass<T>): T

}