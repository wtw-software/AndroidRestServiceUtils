package no.wtw.android.annotation.linky

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Linkys(vararg val value: Linky)