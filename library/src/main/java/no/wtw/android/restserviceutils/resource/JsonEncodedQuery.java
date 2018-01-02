package no.wtw.android.restserviceutils.resource;

import android.util.Base64;
import android.util.Log;

import java.io.Serializable;

import no.wtw.android.restserviceutils.GsonSingleton;

public abstract class JsonEncodedQuery implements Serializable {

    private static final String TAG = JsonEncodedQuery.class.getSimpleName();

    @Override
    public String toString() {
        return encode(true);
    }

    protected String encode(boolean isBase64Encoded) {
        String json = GsonSingleton.getInstance().toJson(this);
        Log.d(TAG, json);
        if (isBase64Encoded)
            return new String(Base64.encode(json.getBytes(), Base64.NO_WRAP));
        return json;
    }

}
