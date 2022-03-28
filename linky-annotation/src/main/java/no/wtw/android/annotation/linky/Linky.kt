package no.wtw.android.annotation.linky

import kotlin.reflect.KClass

@Suppress("DEPRECATED_JAVA_ANNOTATION")
@java.lang.annotation.Repeatable(Linkys::class)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class Linky(
    val linkName: String = "",
    val clazz: KClass<*> = Any::class,
    val methodName: String = "",
)

