package no.wtw.android.restserviceutils.resource;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import no.wtw.android.restserviceutils.exceptions.NoSuchLinkException;

public class Resource implements Serializable, Comparable<Resource> {

    @SerializedName("_links")
    public LinkList links;

    public Link getLink(String type) throws NoSuchLinkException {
        return getLink(null, type);
    }

    public <C, CC extends C> Link<C> getLink(Class<CC> clazz, String type) throws NoSuchLinkException {
        Link link = null;
        if (links != null && links.size() > 0)
            link = getLinkInternal(clazz, type, links);
        if (link == null || TextUtils.isEmpty(link.getUrl()))
            throw new NoSuchLinkException("Link for relation \"" + type + "\" in " + this.getClass().getSimpleName() + " does not exist");
        return link;
    }

    private <C> Link getLinkInternal(Class<C> clazz, String type, List<Link> links) {
        for (Link link : links) {
            if (link.getRelation().equals(type)) {
                link.setClass(clazz);
                return link;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Resource o) {
        return getSortOrder() - o.getSortOrder();
    }

    public int getSortOrder() {
        return 0;
    }

}
