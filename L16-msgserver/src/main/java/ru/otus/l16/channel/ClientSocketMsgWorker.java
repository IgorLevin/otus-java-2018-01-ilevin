package ru.otus.l16.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class ClientSocketMsgWorker extends SocketMsgWorker {

    private final Socket socket;
    private Logger log = LoggerFactory.getLogger(ClientSocketMsgWorker.class);

    public ClientSocketMsgWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    private ClientSocketMsgWorker(Socket socket) throws IOException {
        super(socket);
        this.socket = socket;
    }

    public void close() {
        super.close();
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Closing socket error ", e);
        }
    }
}

