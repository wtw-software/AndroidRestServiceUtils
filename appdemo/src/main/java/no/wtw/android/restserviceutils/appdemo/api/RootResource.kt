package no.wtw.android.restserviceutils.appdemo.api

import no.wtw.android.annotation.linky.Linky
import no.wtw.android.restserviceutils.appdemo.model.MyModel
import no.wtw.android.restserviceutils.resource.Resource

@Linky("deviceRegistration", MyModel::class)
data class RootResource(val a: String) : Resource()
