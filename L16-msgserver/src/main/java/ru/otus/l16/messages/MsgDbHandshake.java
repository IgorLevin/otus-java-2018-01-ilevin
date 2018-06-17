package ru.otus.l16.messages;

import ru.otus.l16.app.Msg;

public class MsgDbHandshake extends Msg {

    public MsgDbHandshake() {
        super(MsgDbHandshake.class);
    }

    @Override
    public String toString() {
        return "MsgDbHandshake";
    }
}
