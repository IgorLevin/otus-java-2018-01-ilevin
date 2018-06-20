package ru.otus.l16;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l16.app.Msg;
import ru.otus.l16.cache.CacheEngine;
import ru.otus.l16.cache.CacheEngineImpl;
import ru.otus.l16.channel.ClientSocketMsgWorker;
import ru.otus.l16.channel.SocketMsgWorker;
import ru.otus.l16.dataset.UserDataSet;
import ru.otus.l16.db.DBService;
import ru.otus.l16.db.DBServiceCachedImpl;
import ru.otus.l16.messages.*;
import ru.otus.l16.server.DBServerMBean;

import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBServerMain implements DBServerMBean {

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static final int PAUSE_MS = 5000;
    private static final int CACHE_SIZE = 5000;

    private CacheEngine<Long, UserDataSet> cacheEngine;

    private static final Logger log = LoggerFactory.getLogger(DBServerMain.class);
    private volatile boolean isRunning;


    public static void main(String[] args) throws Exception {
        new DBServerMain().start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {

        log.info("PID: {}", ManagementFactory.getRuntimeMXBean().getName());

        cacheEngine = new CacheEngineImpl<>(CACHE_SIZE, 0, 0, true);

        try (DBService dbService = new DBServiceCachedImpl(cacheEngine, true)) {
            dbService.init();

            SocketMsgWorker client = new ClientSocketMsgWorker(HOST, PORT);
            client.init();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    while (true) {
                        Msg msg = client.take();
                        processMessage(dbService, client, msg);
                    }
                } catch (InterruptedException e) {
                    log.error("WTF ", e);
                }
            });

            setRunning(true);

            Msg msg = new MsgDbHandshake();
            client.send(msg);

            while (isRunning()) {
                Thread.sleep(PAUSE_MS);
            }
            client.close();
            executorService.shutdownNow();
        }
        log.trace("Done");
    }

    private void processMessage(DBService dbService, SocketMsgWorker client, Msg msg) {
        log.debug("Message received: {}", msg.toString());

        if (msg instanceof MsgGetUserId) {
            MsgGetUserId req = (MsgGetUserId)msg;
            MsgGetUserIdAnswer resp;
            try {
                UserDataSet user = dbService.getUser(req.getLogin());
                resp = new MsgGetUserIdAnswer(user.getName(), user.getId());
            } catch (SQLException e) {
                log.error("Getting user error ", e);
                resp = new MsgGetUserIdAnswer("", -1);
            }
            client.send(resp);
        }
        else if (msg instanceof MsgGetCacheInfo)
        {
            MsgGetCacheInfoAnswer resp = new MsgGetCacheInfoAnswer(cacheEngine.getInfo());
            client.send(resp);
        }
        else if (msg instanceof MsgSetCacheCapacity)
        {
            MsgSetCacheCapacity req = (MsgSetCacheCapacity)msg;
            if (req.getCapacity() > 0) {
                cacheEngine.setCapacity(req.getCapacity());
            }
            MsgGetCacheInfoAnswer resp = new MsgGetCacheInfoAnswer(cacheEngine.getInfo());
            client.send(resp);
        }
        else
        {
            log.info("Unsupported message");
        }
    }

    @Override
    public synchronized boolean isRunning() {
        return isRunning;
    }

    @Override
    public synchronized void setRunning(boolean running) {
        log.info("Set running: {}", running);
        this.isRunning = running;
    }
}

