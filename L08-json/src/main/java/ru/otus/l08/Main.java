package ru.otus.l08;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        log.trace("Custom: {}", CustomJsonWriter.writeObject(new TestObject()));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.trace("Gson: {}", gson.toJson(new TestObject()));
    }

    static class TestObject {
        boolean boolField;
        byte byteField;
        short shortField;
        int intField;
        long longField;
        float floatField;
        double doubleField;
        String stringField;

        int[] intArrayField;
        double[] doubleArrayField;
        String[] stringArrayField;
        List<String> stringListField;
        Map<String, String> stringMapField;
        Map<Integer, String> intMapField;
        Set<String> stringSetField;
        Set<Integer> intSetField;

        TestObject() {
            boolField = true;
            byteField = (byte)1;
            shortField = 2;
            intField = 3;
            longField = 4;
            floatField = 5f;
            doubleField = 6.0;
            stringField = "hello";
            intArrayField = new int[] {1,2,3};
            stringListField = new ArrayList<>();
            stringListField.add("str1");
            stringListField.add("str2");
            stringListField.add("str3");

            stringMapField = new HashMap<>();
            stringMapField.put("k1", "v1");
            stringMapField.put("k2", "v2");
            stringMapField.put("k3", "v3");

            stringSetField = new HashSet<>();
            stringSetField.add("s1");
            stringSetField.add("s2");
            stringSetField.add("s3");

            intMapField = new HashMap<>();
            intMapField.put(1, "i1");
            intMapField.put(2, "i2");
            intMapField.put(3, "i3");

            intSetField = new HashSet<>();
            intSetField.add(1);
            intSetField.add(2);
            intSetField.add(3);
        }
    }
}
