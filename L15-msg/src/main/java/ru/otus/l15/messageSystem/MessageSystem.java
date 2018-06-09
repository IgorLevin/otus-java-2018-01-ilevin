package ru.otus.l15.messageSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author tully
 */
public final class MessageSystem {
    private static final int DEFAULT_STEP_TIME = 10;

    private final List<Thread> workers;
    private final Map<Address, ConcurrentLinkedQueue<Message>> messagesMap;
    private final Map<Address, Addressee> addresseeMap;
    private boolean started;

    private static final Logger log = LoggerFactory.getLogger(MessageSystem.class.getName());

    public MessageSystem() {
        workers = new ArrayList<>();
        messagesMap = new HashMap<>();
        addresseeMap = new HashMap<>();
    }

    public synchronized void addAddressee(Addressee addressee) {
        log.trace("addAddressee: {}", addressee);
        addresseeMap.put(addressee.getAddress(), addressee);
        messagesMap.put(addressee.getAddress(), new ConcurrentLinkedQueue<>());
    }

    public synchronized void sendMessage(Message message) {
        log.trace("sendMessage: {}", message);
        if (!started) {
            start();
        }
        messagesMap.get(message.getTo()).add(message);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void start() {
        log.trace("start()");
        for (Map.Entry<Address, Addressee> entry : addresseeMap.entrySet()) {
            String name = "MS-worker-" + entry.getKey().getId();
            Thread thread = new Thread(() -> {
                while (true) {

                    ConcurrentLinkedQueue<Message> queue = messagesMap.get(entry.getKey());
                    while (!queue.isEmpty()) {
                        Message message = queue.poll();
                        log.trace("Got message in the queue: {}", message);
                        message.exec(entry.getValue());
                    }
                    try {
                        Thread.sleep(MessageSystem.DEFAULT_STEP_TIME);
                    } catch (InterruptedException e) {
                        log.info("Thread interrupted. Finishing: {}", name);
                        return;
                    }
                    if (Thread.currentThread().isInterrupted()) {
                        log.info("Finishing: {}", name);
                        return;
                    }
                }
            });
            thread.setName(name);
            thread.start();
            workers.add(thread);
        }
        started = true;
    }

    public synchronized void dispose() {
        log.trace("dispose()");
        workers.forEach(Thread::interrupt);
        started = false;
    }
}
