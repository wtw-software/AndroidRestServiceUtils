package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceCollection<D extends Comparable<? super D>> extends Resource {

    @SerializedName("resource")
    private List<D> resources;

    private boolean isSorted;

    public List<D> getResources() {
        if (!isSorted) {
            List<D> tmpList = new ArrayList<>(resources);
            Collections.sort(tmpList);
            resources = tmpList;
            isSorted = true;
        }
        return resources;
    }

}
