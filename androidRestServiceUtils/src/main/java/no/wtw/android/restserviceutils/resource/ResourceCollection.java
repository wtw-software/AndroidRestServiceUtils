package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceCollection<D extends Comparable<? super D>> extends Resource {

    @SerializedName("resource")
    private List<D> resources;

    public List<D> getResources() {
        // Prevent java.util.ConcurrentModificationException by creating a new temp list for each time.
        List<D> newList = new ArrayList<>(resources);
        Collections.sort(newList);
        return newList;
    }

}
