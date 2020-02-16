package no.wtw.android.restserviceutils.appdemo

import no.wtw.android.annotation.linky.Linky
import no.wtw.android.restserviceutils.resource.Resource

@Linky(linkName = "deviceRegistration", clazz = String::class)
//@Linky(linkName = "deviceRegistration2", clazz = DeviceRegistrationResponse::class)
data class RootResource(val a: String) : Resource()
