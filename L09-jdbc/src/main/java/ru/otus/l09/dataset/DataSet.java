package ru.otus.l09.dataset;

/**
 * Abstract base class used by {@link CustomExecutor}
 */
public abstract class DataSet {

    private long id = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
