package ru.otus.l12;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l12.servlet.AdminServlet;
import ru.otus.l12.servlet.LoginServlet;
import ru.otus.l12.servlet.TemplateProcessor;

/**
 * Jetty web server as cache web interface
 *
 * To run H2 browser console run $PROJECT_DIR/h2db/h2_web_console.sh(bat)
 */

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "html";

    public static void main(String[] args) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(new ServletHolder(new LoginServlet(templateProcessor, "anonymous")), "/login");
        context.addServlet(AdminServlet.class, "/admin");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
