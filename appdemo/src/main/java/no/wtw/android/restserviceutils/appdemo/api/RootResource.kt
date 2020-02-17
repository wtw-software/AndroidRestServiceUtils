package no.wtw.android.restserviceutils.appdemo.api

import no.wtw.android.annotation.linky.Linky
import no.wtw.android.annotation.linky.Linkys
import no.wtw.android.restserviceutils.appdemo.model.MyModel
import no.wtw.android.restserviceutils.resource.Resource

@Linkys(
        Linky("deviceRegistration", MyModel::class),
        Linky("split-word", MyModel::class),
        Linky("snake_case", MyModel::class),
        Linky("login", Any::class),
        Linky("icon")
)
data class RootResource(val a: String) : Resource()
