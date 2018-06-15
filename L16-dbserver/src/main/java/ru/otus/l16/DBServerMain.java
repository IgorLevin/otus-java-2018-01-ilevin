package ru.otus.l16;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l16.app.Msg;
import ru.otus.l16.channel.ClientSocketMsgWorker;
import ru.otus.l16.channel.SocketMsgWorker;
import ru.otus.l16.messages.PingMsg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBServerMain {

    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static final int PAUSE_MS = 5000;
    private static final int MAX_MESSAGES_COUNT = 2;

    private static final Logger log = LoggerFactory.getLogger(DBServerMain.class);


    public static void main(String[] args) throws Exception {
        new DBServerMain().start();
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() throws Exception {
        SocketMsgWorker client = new ClientSocketMsgWorker(HOST, PORT);
        client.init();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
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

        int count = 0;
        while (count < MAX_MESSAGES_COUNT) {
            Msg msg = new PingMsg();
            client.send(msg);
            log.debug("Message sent: {}", msg.toString());
            Thread.sleep(PAUSE_MS);
            count++;
        }
        client.close();
        executorService.shutdownNow();
        log.trace("Done");
    }
}

