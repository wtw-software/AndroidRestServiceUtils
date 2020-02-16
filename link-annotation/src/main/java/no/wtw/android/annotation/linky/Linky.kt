package no.wtw.android.annotation.linky

import kotlin.reflect.KClass

@Repeatable
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Linky(
    val linkName: String = "",
    val clazz: KClass<*>
)