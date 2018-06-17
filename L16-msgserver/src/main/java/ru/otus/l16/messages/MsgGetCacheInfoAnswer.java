package ru.otus.l16.messages;

import ru.otus.l16.app.CacheInfo;
import ru.otus.l16.app.Msg;

public class MsgGetCacheInfoAnswer  extends Msg {

    private CacheInfo info;

    MsgGetCacheInfoAnswer() {
        super(MsgGetCacheInfoAnswer.class);
    }

    public MsgGetCacheInfoAnswer(CacheInfo info) {
        super(MsgGetCacheInfoAnswer.class);
        this.info = info;
    }

    public CacheInfo getInfo() {
        return info;
    }

    public void setInfo(CacheInfo info) {
        this.info = info;
    }

}
