package ru.otus.l16.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.l16.app.LoginProcessListener;
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
 *
 */
public class LoginServlet extends HttpServlet implements LoginProcessListener {

    public static final String LOGIN_PARAMETER_NAME = "login";
    public static final String SESSION_ATTRIBUTE_NAME = "login";

    private static final String MSG_VARIABLE_NAME = "msg";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";

    @Autowired
    private FrontendService frontendService;
    @Autowired
    private TemplateProcessor templateProcessor;

    private Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private boolean userExists;
    private boolean requestComplete;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    private String getLoginPage(String msg) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(MSG_VARIABLE_NAME, msg);
        return templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        log.trace("Query: {}", request.getQueryString());

        String requestLogin = request.getParameter(LOGIN_PARAMETER_NAME);

        log.trace("Request login: {}", requestLogin);

        userExists = false;

        String page;
        if (requestLogin == null) {
            page = getLoginPage("");
            request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME, null);
        } else {
            requestComplete = false;
            login(requestLogin, "");

            synchronized (this) {
                while (!requestComplete) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        requestComplete = true;
                    }
                }
            }

            if (userExists) {
                request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME, requestLogin);
                response.sendRedirect(request.getContextPath() + "/admin");
                return;
            } else {
                page = getLoginPage("Пользователь не существует");
            }
        }
        setOK(response);
        response.getWriter().println(page);
    }

    private void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void login(String login, String password) {
        log.trace("login");
        frontendService.loginUser(login, password,this);
    }


    @Override
    public void userLogged() {
        synchronized (this) {
            requestComplete = true;
            userExists = true;
            notify();
        }
    }

    @Override
    public void wrongUser() {
        synchronized (this) {
            requestComplete = true;
            userExists = false;
            notify();
        }
    }
}
