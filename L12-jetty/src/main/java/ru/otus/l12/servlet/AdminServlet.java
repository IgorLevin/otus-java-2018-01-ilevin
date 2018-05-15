package ru.otus.l12.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l12.cache.CacheEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tully.
 */
public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";

    private static final String USER_VARIABLE_NAME = "login";
    private static final String CACHE_CAPACITY_VARIABLE_NAME = "cacheCapacity";
    private static final String CACHE_NEW_CAPACITY_VARIABLE_NAME = "newCacheCapacity";

    private final TemplateProcessor templateProcessor;
    private CacheEngine<?, ?> cacheEngine;

    private Logger log = LoggerFactory.getLogger(AdminServlet.class);

    @SuppressWarnings("WeakerAccess")
    public AdminServlet(TemplateProcessor templateProcessor, CacheEngine<?, ?> cacheEngine) {
        this.templateProcessor = templateProcessor;
        this.cacheEngine = cacheEngine;
    }

    private String getAdminPage(String user) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(CACHE_CAPACITY_VARIABLE_NAME, cacheEngine.capacity());
        pageVariables.put(USER_VARIABLE_NAME, user);
        return templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String login = (String)request.getSession().getAttribute(LoginServlet.SESSION_ATTRIBUTE_NAME);
        if (login == null || login.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String newCapacity = request.getParameter(CACHE_NEW_CAPACITY_VARIABLE_NAME);
        if (newCapacity != null && !newCapacity.isEmpty()) {
            try {
                Integer capacity = Integer.valueOf(newCapacity);
                cacheEngine.setCapacity(capacity);
            } catch (NumberFormatException e) {
                log.error("Wrong cache capacity value");
            }
        }

        response.setContentType("text/html;charset=utf-8");
        String page = getAdminPage(login);
        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
