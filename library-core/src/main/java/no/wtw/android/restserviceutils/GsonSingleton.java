package no.wtw.android.restserviceutils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.time.ZonedDateTime;

import no.wtw.android.restserviceutils.serializer.DateTimeSerializer;
import no.wtw.android.restserviceutils.serializer.LocalDateSerializer;
import no.wtw.android.restserviceutils.serializer.ZonedDateTimeSerializer;

public class GsonSingleton {

    private static final Gson GSON_INSTANCE =
        new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer())
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public static Gson getInstance() {
        return GSON_INSTANCE;
    }

}
