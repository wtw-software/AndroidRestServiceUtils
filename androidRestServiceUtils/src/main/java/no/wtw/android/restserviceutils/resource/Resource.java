package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import no.wtw.android.restserviceutils.exceptions.NoSuchLinkException;

public class Resource implements Serializable, Comparable<Resource> {

    @SerializedName("_links")
    private List<Link> links;

    protected Link getLink(String type) throws NoSuchLinkException {
        return getLink(null, type);
    }

    protected <C, CC extends C> Link<C> getLink(Class<CC> clazz, String type) throws NoSuchLinkException {
        Link link = null;
        if (links != null && links.size() > 0)
            link = getLinkInternal(clazz, type, links);
        if (link == null)
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
        return 0;
    }

}
