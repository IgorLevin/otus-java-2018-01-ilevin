package ru.otus.l12.servlet;

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
    private static final String LOGIN_VARIABLE_NAME = "login";

    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String WRONG_LOGIN_PAGE_TEMPLATE = "wrong_login.html";
    private static final String ADMIN_LOGIN = "admin";

    private final TemplateProcessor templateProcessor;
    private String login;

    public LoginServlet(TemplateProcessor templateProcessor, String login) {
        this.login = login;
        this.templateProcessor = templateProcessor;
    }

    private String getPage(String login) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(LOGIN_VARIABLE_NAME, login == null ? "" : login);
        return templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, pageVariables);
    }

    private String getPageWrongLogin() throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        return templateProcessor.getPage(WRONG_LOGIN_PAGE_TEMPLATE, pageVariables);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String requestLogin = request.getParameter(LOGIN_PARAMETER_NAME);

        String page;
        if (requestLogin == null || ADMIN_LOGIN.equals(requestLogin)) {
            page = getPage(login); //save to the page
        } else {
            page = getPageWrongLogin();
        }

//        if (requestLogin != null) {
//            saveToVariable(requestLogin);
//            saveToSession(request, requestLogin); //request.getSession().getAttribute("login");
//            saveToServlet(request, requestLogin); //request.getAttribute("login");
//            saveToCookie(response, requestLogin); //request.getCookies();
//        }
        setOK(response);
        response.getWriter().println(page);
    }

    private void saveToCookie(HttpServletResponse response, String requestLogin) {
        response.addCookie(new Cookie("L12.1-login", requestLogin));
    }

    private void saveToServlet(HttpServletRequest request, String requestLogin) {
        request.getServletContext().setAttribute("login", requestLogin);
    }

    private void saveToSession(HttpServletRequest request, String requestLogin) {
        request.getSession().setAttribute("login", requestLogin);
    }

    private void saveToVariable(String requestLogin) {
        login = requestLogin != null ? requestLogin : login;
    }

    private void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
