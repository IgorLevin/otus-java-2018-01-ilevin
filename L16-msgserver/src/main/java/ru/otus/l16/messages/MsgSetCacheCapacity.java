package ru.otus.l16.messages;

import ru.otus.l16.app.Msg;

public class MsgSetCacheCapacity extends Msg {

    private final int capacity;

    public MsgSetCacheCapacity(int capacity) {
        super(MsgSetCacheCapacity.class);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}