package ru.otus.l12.servlet;

import ru.otus.l12.cache.CacheEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheStateServlet extends HttpServlet {

    private static final String CACHE_SIZE = "size";
    private static final String CACHE_HIT = "hit";
    private static final String CACHE_MISS = "miss";

    private final TemplateProcessor templateProcessor;
    private CacheEngine<?, ?> cacheEngine;

    @SuppressWarnings("WeakerAccess")
    public CacheStateServlet(TemplateProcessor templateProcessor, CacheEngine<?, ?> cacheEngine) {
        this.templateProcessor = templateProcessor;
        this.cacheEngine = cacheEngine;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String login = (String)request.getSession().getAttribute(LoginServlet.SESSION_ATTRIBUTE_NAME);
        if (login == null || login.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(CACHE_SIZE, cacheEngine.size());
        pageVariables.put(CACHE_HIT, cacheEngine.getHitCount());
        pageVariables.put(CACHE_MISS, cacheEngine.getMissCount());
        response.getWriter().println(templateProcessor.getPage("cache_state.json", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

