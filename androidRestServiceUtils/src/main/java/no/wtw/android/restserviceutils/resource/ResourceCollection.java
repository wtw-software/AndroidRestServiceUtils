package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResourceCollection<D> extends Resource {

    @SerializedName("resource")
    private List<D> resources;

    public List<D> getResources() {
        return resources;
    }

}
