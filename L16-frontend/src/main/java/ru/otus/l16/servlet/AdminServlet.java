package ru.otus.l16.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.l16.front.FrontendService;

import javax.servlet.ServletConfig;
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
    private static final String CACHE_NEW_CAPACITY_VARIABLE_NAME = "newCacheCapacity";

    @Autowired
    private TemplateProcessor templateProcessor;
    @Autowired
    private FrontendService frontendService;

    private Logger log = LoggerFactory.getLogger(AdminServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    private String getAdminPage(String user) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(USER_VARIABLE_NAME, user);
        return templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = (String)request.getSession().getAttribute(LoginServlet.SESSION_ATTRIBUTE_NAME);
        if (login == null || login.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String newCapacity = request.getParameter(CACHE_NEW_CAPACITY_VARIABLE_NAME);
        if (newCapacity != null && !newCapacity.isEmpty()) {
            try {
                Integer capacity = Integer.valueOf(newCapacity);
                frontendService.setCacheCapacity(capacity);
            } catch (NumberFormatException e) {
                log.error("Wrong cache capacity value");
            }
        }
        fillInResponseWithPage(login, response);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String login = (String)request.getSession().getAttribute(LoginServlet.SESSION_ATTRIBUTE_NAME);
        if (login == null || login.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        fillInResponseWithPage(login, response);
    }

    private void fillInResponseWithPage(String login, HttpServletResponse response) throws IOException {
        String page = getAdminPage(login);
        response.getWriter().println(page);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
