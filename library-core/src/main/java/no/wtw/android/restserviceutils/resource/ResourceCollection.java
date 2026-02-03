package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceCollection<D extends Comparable<? super D>> extends Resource {

    @SerializedName("resource")
    protected List<D> items;

    protected boolean isSorted;

    public List<D> getItems() {
        if (!isSorted) {
            List<D> tmpList = new ArrayList<>(items);
            Collections.sort(tmpList);
            items = tmpList;
            isSorted = true;
        }
        return items;
    }

}
