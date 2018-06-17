package ru.otus.l16.app;

/**
 * Created by tully.
 */
public interface MsgWorker {
    void send(Msg msg);

    Msg pool();

    Msg take() throws InterruptedException;

    void close();

    boolean isDbServiceConnection();

    void setDbServiceConnection(boolean isDbConnection);
}
