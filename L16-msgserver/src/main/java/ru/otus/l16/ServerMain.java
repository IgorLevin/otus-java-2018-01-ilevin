package ru.otus.l16;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l16.server.SocketMsgServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by tully.
 */
public class ServerMain {

    private static final Logger log = LoggerFactory.getLogger(ServerMain.class);

    //private static final String CLIENT_START_COMMAND = "java -jar ../L16.1.2-client/target/client.jar";
    private static final int CLIENT_START_DELAY_SEC = 5;

    public static void main(String[] args) throws Exception {
        new ServerMain().start();
    }

    private void start() throws Exception {

        log.info("PID: {}", ManagementFactory.getRuntimeMXBean().getName());

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        startClient(executorService);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Server");
        SocketMsgServer server = new SocketMsgServer();
        mbs.registerMBean(server, name);

        server.start();

        executorService.shutdown();
    }

    private void startClient(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
//            try {
//                new ProcessRunnerImpl().start(CLIENT_START_COMMAND);
//            } catch (IOException e) {
//                logger.log(Level.SEVERE, e.getMessage());
//            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }

}

