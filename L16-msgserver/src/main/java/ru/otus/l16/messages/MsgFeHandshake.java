package ru.otus.l16.messages;

import ru.otus.l16.app.Msg;

public class MsgFeHandshake extends Msg {

    public MsgFeHandshake() {
        super(MsgFeHandshake.class);
    }

    @Override
    public String toString() {
        return "MsgFeHandshake";
    }
}
