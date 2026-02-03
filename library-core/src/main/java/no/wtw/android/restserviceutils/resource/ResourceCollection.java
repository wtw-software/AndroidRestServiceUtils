package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceCollection<D extends Sortable> extends Resource {

    @SerializedName("resource")
    protected List<D> items;

    protected boolean isSorted;

    public List<D> getItems() {
        if (!isSorted) {
            List<D> tmpList = new ArrayList<>(items);
            Collections.sort(tmpList, (o1, o2) -> {
                int i1 = (o1 == null) ? 0 : o1.getSortOrder();
                int i2 = (o2 == null) ? 0 : o2.getSortOrder();
                return i1 - i2;
            });
            items = tmpList;
            isSorted = true;
        }
        return items;
    }

}

interface Sortable {
    int getSortOrder();
}