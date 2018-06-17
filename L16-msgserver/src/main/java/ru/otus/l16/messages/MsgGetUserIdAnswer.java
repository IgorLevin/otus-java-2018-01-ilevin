package ru.otus.l16.messages;

import ru.otus.l16.app.Msg;

/**
 * Created by tully.
 */
public class MsgGetUserIdAnswer extends Msg {

    private final String name;
    private final long id;

    public MsgGetUserIdAnswer(String name, long id) {
        super(MsgGetUserIdAnswer.class);
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
