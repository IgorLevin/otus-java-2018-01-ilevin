package ru.otus.l11.dataset;

public abstract class DataSet {

    private long id = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
