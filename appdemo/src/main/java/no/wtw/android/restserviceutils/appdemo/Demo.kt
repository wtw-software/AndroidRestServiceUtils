package no.wtw.android.restserviceutils.appdemo

import no.wtw.android.restserviceutils.appdemo.api.RootResource
import no.wtw.android.restserviceutils.appdemo.api.getLoginLink

fun init() {

    val a = RootResource("asdf")

    val loginLink = a.getLoginLink()

}