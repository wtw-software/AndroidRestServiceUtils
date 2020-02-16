package no.wtw.android.annotation.linky

import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

@Repeatable
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Linky(
        val linkName: String = "",
        val clazz: KClass<*>
)

fun Linky.getClazz(): TypeMirror {
    try {
        this.clazz
    } catch (mte: MirroredTypeException) {
        return mte.typeMirror
    }
    throw RuntimeException("Type fetching failed")
}