package ru.otus.l08.jsonwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;
import ru.otus.l08.data.CustomObject;

public class CustomJsonWriterTest {


    @Test
    public void writeObject() {
        try {
            CustomObject in = new CustomObject();
            String json = CustomJsonWriter.writeObject(in);
            Gson gson = new GsonBuilder().create();
            CustomObject out = gson.fromJson(json, CustomObject.class);
            Assert.assertEquals(in, out);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}