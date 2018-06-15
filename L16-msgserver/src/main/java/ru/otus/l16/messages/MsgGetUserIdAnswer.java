package ru.otus.l16.messages;

import ru.otus.l16.app.Msg;

/**
 * Created by tully.
 */
public class MsgGetUserIdAnswer extends Msg {

    private final String name;
    private final int id;

    public MsgGetUserIdAnswer(String name, int id) {
        super(MsgGetUserIdAnswer.class);
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
