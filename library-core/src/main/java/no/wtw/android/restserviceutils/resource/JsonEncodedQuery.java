package no.wtw.android.restserviceutils.resource;

import org.springframework.util.support.Base64;

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
        System.out.println(json);
        if (isBase64Encoded)
            return Base64.encodeBytes(json.getBytes());
        return json;
    }

}
