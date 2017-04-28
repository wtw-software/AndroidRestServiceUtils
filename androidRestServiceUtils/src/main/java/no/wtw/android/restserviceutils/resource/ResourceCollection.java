package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class ResourceCollection<D extends Comparable<D>> extends Resource {

    @SerializedName("resource")
    private List<D> resources;

    public List<D> getResources() {
        Collections.sort(resources);
        return resources;
    }

}
