package ru.otus.l16.server;

public interface DBServerMBean {
    boolean isRunning();
    void setRunning(boolean running);
}
