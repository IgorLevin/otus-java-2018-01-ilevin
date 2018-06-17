package ru.otus.l16.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l16.app.ProcessRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tully.
 */
public class ProcessRunnerImpl implements ProcessRunner {

    private final StringBuffer out = new StringBuffer();
    private Process process;

    private final Logger log = LoggerFactory.getLogger(ProcessRunnerImpl.class);

    public void start(String command) throws IOException {
        process = runProcess(command);
        log.info("PID: {}", process.pid());
    }

    public void stop() {
        process.destroy();
    }

    public String getOutput() {
        return out.toString();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        Process p = pb.start();

        StreamListener output = new StreamListener(p.getInputStream(), "OUTPUT");
        output.start();

        return p;
    }

    private class StreamListener extends Thread {

        private final Logger log = LoggerFactory.getLogger(StreamListener.class);

        private final InputStream is;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    out.append(type).append('>').append(line).append('\n');
                    log.trace("{}: {}", type, line);
                }
            } catch (IOException e) {
                log.error("Reading log stream error ", e);
            }
        }
    }
}
