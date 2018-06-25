package ru.otus.l08;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l08.data.CustomObject;
import ru.otus.l08.jsonwriter.CustomJsonWriter;

import java.util.*;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {

        try {
            Gson gson = new GsonBuilder().create();

            log.trace("Custom: {}", CustomJsonWriter.writeObject(5));
            log.trace("  Gson: {}", gson.toJson(5));

            log.trace("Custom: {}", CustomJsonWriter.writeObject("Hello JSON"));
            log.trace("  Gson: {}", gson.toJson("Hello JSON"));

            Map<Integer, String> mapIntStr = new HashMap<>();
            mapIntStr.put(1, "ggg");
            mapIntStr.put(2, "hhh");
            mapIntStr.put(3, "mmm");

            log.trace("Custom: {}", CustomJsonWriter.writeObject(mapIntStr));
            log.trace("  Gson: {}", gson.toJson(mapIntStr));

            log.trace("Custom: {}", CustomJsonWriter.writeObject(new CustomObject()));
            log.trace("  Gson: {}", gson.toJson(new CustomObject()));

        } catch (Exception e) {
            log.error("WTF", e);
        }
    }
}
