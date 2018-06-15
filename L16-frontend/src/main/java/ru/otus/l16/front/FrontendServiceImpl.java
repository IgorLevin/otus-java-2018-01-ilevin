package ru.otus.l16.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l16.app.CacheInfo;
import ru.otus.l16.app.CacheInfoListener;
import ru.otus.l16.app.LoginProcessListener;
import ru.otus.l16.app.Msg;
import ru.otus.l16.channel.ClientSocketMsgWorker;
import ru.otus.l16.channel.SocketMsgWorker;
import ru.otus.l16.messages.MsgGetCacheInfo;
import ru.otus.l16.messages.MsgGetUserId;
import ru.otus.l16.messages.MsgSetCacheCapacity;

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

    private Logger log = LoggerFactory.getLogger(FrontendServiceImpl.class);

    @Override
    public void init() throws Exception {
        client = new ClientSocketMsgWorker(HOST, PORT);
        client.init();

        executorService.submit(() -> {
            try {
                while (true) {
                    Object msg = client.take();
                    log.debug("Message received: {}", msg.toString());
                }
            } catch (InterruptedException e) {
                log.error("WTF ", e);
            }
        });

//        int count = 0;
//        while (count < MAX_MESSAGES_COUNT) {
//            Msg msg = new PingMsg();
//            client.send(msg);
//            log.debug("Message sent: {}", msg.toString());
//            Thread.sleep(PAUSE_MS);
//            count++;
//        }
    }

    @Override
    public void close() {
        client.close();
        executorService.shutdownNow();
        log.trace("close");
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
    public void getCacheInfo(CacheInfoListener listener) {
        log.trace("getCacheInfo");
        Msg msg = new MsgGetCacheInfo();
        sendMessage(msg);
    }

    @Override
    public void onCacheInfo(CacheInfo info, CacheInfoListener listener) {
        if (listener != null) {
            listener.onCacheInfo(info);
        }
    }

    private synchronized void sendMessage(Msg msg) {
        if (client != null) {
            client.send(msg);
        }
    }
}
