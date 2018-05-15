package ru.otus.l12.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l12.cache.CacheEngine;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author v.chibrikov
 */
public class LoginServlet extends HttpServlet {

    public static final String LOGIN_PARAMETER_NAME = "login";
    public static final String SESSION_ATTRIBUTE_NAME = "login";

    private static final String MSG_VARIABLE_NAME = "msg";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String ADMIN_LOGIN = "admin";

    private final TemplateProcessor templateProcessor;

    private Logger log = LoggerFactory.getLogger(LoginServlet.class);

    public LoginServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
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

        String page;
        if (requestLogin == null) {
            page = getLoginPage("");
            request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME, null);
        } else if (ADMIN_LOGIN.equals(requestLogin)){
            request.getSession().setAttribute(SESSION_ATTRIBUTE_NAME, requestLogin);
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        } else {
            page = getLoginPage("Пользователь не существует");
        }

        setOK(response);
        response.getWriter().println(page);
    }

    private void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
