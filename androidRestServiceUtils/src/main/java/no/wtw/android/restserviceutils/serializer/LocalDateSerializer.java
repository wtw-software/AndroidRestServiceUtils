package no.wtw.android.restserviceutils.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;

import no.wtw.android.restserviceutils.TimeFormatUtil;

public class LocalDateSerializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return TimeFormatUtil.getRFC822LocalDateFormatter().parseLocalDate(json.getAsString());
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(TimeFormatUtil.getRFC822LocalDateFormatter().print(src));
    }
}
