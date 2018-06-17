package ru.otus.l16.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l16.app.CacheInfo;
import ru.otus.l16.app.CacheInfoListener;
import ru.otus.l16.app.LoginProcessListener;
import ru.otus.l16.app.Msg;
import ru.otus.l16.channel.ClientSocketMsgWorker;
import ru.otus.l16.channel.SocketMsgWorker;
import ru.otus.l16.messages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrontendServiceImpl implements FrontendService {

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static final int PAUSE_MS = 5000;
    private static final int MAX_MESSAGES_COUNT = 2;

    private SocketMsgWorker client;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private LoginProcessListener loginProcessListener;
    private List<CacheInfoListener> cacheInfoListeners = new ArrayList<>();

    private Logger log = LoggerFactory.getLogger(FrontendServiceImpl.class);

    @Override
    public void init() throws Exception {
        client = new ClientSocketMsgWorker(HOST, PORT);
        client.init();

        executorService.submit(() -> {
            try {
                while (true) {
                    Msg msg = client.take();
                    processMessage(msg);
                }
            } catch (InterruptedException e) {
                log.error("WTF ", e);
            }
        });
    }

    @Override
    public void close() {
        client.close();
        executorService.shutdownNow();
        log.trace("close");
    }

    private void processMessage(Msg msg) {
        log.debug("Message received: {}", msg.toString());

        if (msg instanceof MsgGetUserIdAnswer) {
            MsgGetUserIdAnswer resp = (MsgGetUserIdAnswer)msg;
            if (resp.getId() == -1) {
                onWrongUser();
            } else {
                onUserLogged();
            }
        }
        else if (msg instanceof MsgGetCacheInfoAnswer)
        {
            MsgGetCacheInfoAnswer resp = (MsgGetCacheInfoAnswer)msg;
            onCacheInfo(resp.getInfo());
        }
        else
        {
            log.info("Unsupported message");
        }
    }

    @Override
    public void loginUser(String login, String password, LoginProcessListener listener) {
        log.trace("loginUser: {}, {}, {}", login, password, loginProcessListener);
        this.loginProcessListener = listener;
        Msg msg = new MsgGetUserId(login, password);
        sendMessage(msg);
    }

    @Override
    public void onUserLogged() {
        log.trace("onUserLogged()");
        if (loginProcessListener != null) {
            loginProcessListener.userLogged();
        }
    }

    @Override
    public void onWrongUser() {
        log.trace("onWrongUser()");
        if (loginProcessListener != null) {
            loginProcessListener.wrongUser();
        }
    }

    @Override
    public void setCacheCapacity(int capacity) {
        Msg msg = new MsgSetCacheCapacity(capacity);
        sendMessage(msg);
    }

    @Override
    public synchronized void getCacheInfo(CacheInfoListener listener) {
        log.trace("getCacheInfo");
        if (!cacheInfoListeners.contains(listener)) {
            cacheInfoListeners.add(listener);
        }
        Msg msg = new MsgGetCacheInfo();
        sendMessage(msg);
    }

    @Override
    public synchronized void onCacheInfo(CacheInfo info) {
        for (CacheInfoListener listener : cacheInfoListeners) {
            if (listener != null) {
                listener.onCacheInfo(info);
            }
        }
    }

    private synchronized void sendMessage(Msg msg) {
        if (client != null) {
            client.send(msg);
        }
    }
}
