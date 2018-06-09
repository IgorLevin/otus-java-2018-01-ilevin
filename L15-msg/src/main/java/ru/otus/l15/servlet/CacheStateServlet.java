package ru.otus.l15.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.l15.app.CacheInfo;
import ru.otus.l15.app.CacheInfoListener;
import ru.otus.l15.front.FrontendService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CacheStateServlet extends HttpServlet implements CacheInfoListener {

    private static final String CACHE_SIZE = "size";
    private static final String CACHE_HIT = "hit";
    private static final String CACHE_MISS = "miss";
    private static final String CACHE_CAPACITY = "capacity";

    private CacheInfo cacheInfo;
    @Autowired
    private TemplateProcessor templateProcessor;
    @Autowired
    private FrontendService frontendService;

    private Logger log = LoggerFactory.getLogger(CacheStateServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    @SuppressWarnings("EmptyCatchBlock")
    public void destroy() {
        super.destroy();
        log.trace("Destroy");
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = (String)request.getSession().getAttribute(LoginServlet.SESSION_ATTRIBUTE_NAME);
        if (login == null || login.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        getCacheInfo();

        Map<String, Object> pageVariables = new HashMap<>();
        synchronized (this)
        {
            pageVariables.put(CACHE_SIZE, cacheInfo.getSize());
            pageVariables.put(CACHE_HIT, cacheInfo.getHitCount());
            pageVariables.put(CACHE_MISS, cacheInfo.getMissCount());
            pageVariables.put(CACHE_CAPACITY, cacheInfo.getCapacity());
        }
        response.getWriter().println(templateProcessor.getPage("cache_state.json", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getCacheInfo() {
        log.trace("getCacheInfo");
        frontendService.getCacheInfo(this);
    }

    @Override
    public void onCacheInfo(CacheInfo info) {
        log.trace("onCacheInfo: {}", info);
        synchronized (this) {
            cacheInfo = info;
        }
    }
}

