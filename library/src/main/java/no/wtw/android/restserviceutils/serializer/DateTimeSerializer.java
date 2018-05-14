package no.wtw.android.restserviceutils.serializer;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

import no.wtw.android.restserviceutils.TimeFormatUtil;

public class DateTimeSerializer implements JsonDeserializer<DateTime>, JsonSerializer<DateTime> {

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || TextUtils.isEmpty(json.getAsString()))
            return null;
        return TimeFormatUtil.getRFC822DateTimeFormatter().parseDateTime(json.getAsString());
    }

    @Override
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(TimeFormatUtil.getRFC822DateTimeFormatter().print(src));
    }

}