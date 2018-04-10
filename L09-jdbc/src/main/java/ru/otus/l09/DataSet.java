package ru.otus.l09;

import java.util.Random;

/**
 * Abstract base class used by {@link CustomExecutor}
 */
public abstract class DataSet {

    private long id;

    DataSet() {
        Random rnd = new Random();
        id = rnd.nextLong();
    }

    DataSet(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
