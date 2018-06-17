package ru.otus.l16.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l16.app.Msg;
import ru.otus.l16.app.MsgWorker;
import ru.otus.l16.channel.SocketMsgWorker;
import ru.otus.l16.messages.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketMsgServer implements SocketMsgServerMBean {

    private static final int PORT = 5050;
    private static final int NUM_OF_THREADS = 1;
    private static final int MSG_PROCESSING_DELAY_MS = 100;

    private final ExecutorService executor;
    private final List<MsgWorker> workers;

    private Logger log = LoggerFactory.getLogger(SocketMsgServer.class);

    public SocketMsgServer() {
        executor = Executors.newFixedThreadPool(NUM_OF_THREADS);
        workers = new CopyOnWriteArrayList<>();
    }

    public void start() throws Exception {
        executor.submit(this::doWork);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info("Server started on port: {}", serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket socket = serverSocket.accept(); //blocks
                log.info("Incoming connection from: {}", socket.getInetAddress().getHostAddress());
                SocketMsgWorker client = new SocketMsgWorker(socket);
                client.init();
                workers.add(client);
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void doWork() {
        while (true) {
            for (MsgWorker client : workers) {
                Msg msg = client.pool();
                while (msg != null) {
                    log.trace("Got message: {}", msg);
                    if (msg instanceof MsgDbHandshake) {
                        log.trace("Handshake from DB");
                        client.setDbServiceConnection(true);
                    }
                    else if (msg instanceof MsgFeHandshake)
                    {
                        log.trace("Handshake from FE");
                        client.setDbServiceConnection(false);
                    }
                    else if (msg instanceof MsgGetUserId ||
                             msg instanceof MsgGetCacheInfo ||
                             msg instanceof MsgSetCacheCapacity)
                    {
                        for (MsgWorker worker : workers) {
                            if (worker.isDbServiceConnection()) {
                                worker.send(msg);
                            }
                        }
                    }
                    else if (msg instanceof MsgGetUserIdAnswer ||
                             msg instanceof MsgGetCacheInfoAnswer)
                    {
                        for (MsgWorker worker : workers) {
                            if (!worker.isDbServiceConnection()) {
                                worker.send(msg);
                            }
                        }
                    }
                    else {
                        client.send(msg);  // эхо
                    }
                    msg = client.pool();
                }
            }
            try {
                Thread.sleep(MSG_PROCESSING_DELAY_MS);
            } catch (InterruptedException e) {
                log.error("Interrupted ", e);
            }

        }
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public void setRunning(boolean running) {
        if (!running) {
            for (MsgWorker worker : workers) {
                worker.close();
            }
            executor.shutdown();
            log.info("Shutdown");
        }
    }
}
