package no.wtw.android.restserviceutils.resource

import no.wtw.android.restserviceutils.exceptions.NoSuchLinkException
import java.io.Serializable

abstract class ResourceKt : Serializable, Sortable {

    abstract val links: LinkList?

    protected inline fun <reified T : Any> getLink(type: String): Link<T> =
        getLinkOrNull<T>(type)
            ?: throw NoSuchLinkException("Link for relation '$type' in ${this::class.simpleName} does not exist")

    @Suppress("UNCHECKED_CAST")
    protected inline fun <reified T : Any> getLinkOrNull(type: String): Link<T>? {
        val link = links?.firstOrNull { it.relation == type } as? Link<T> ?: return null
        link.setClass(T::class.java)
        return link
    }

    override fun getSortOrder() = 0

}