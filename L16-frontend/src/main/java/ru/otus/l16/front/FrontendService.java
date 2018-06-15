package ru.otus.l16.front;

import ru.otus.l16.app.CacheInfo;
import ru.otus.l16.app.CacheInfoListener;
import ru.otus.l16.app.LoginProcessListener;

public interface FrontendService {

    void init() throws Exception;

    void close();

    void loginUser(String login, String password, LoginProcessListener service);

    void onUserLogged();

    void onWrongUser();

    void setCacheCapacity(int capacity);

    void getCacheInfo(CacheInfoListener listener);

    void onCacheInfo(CacheInfo info, CacheInfoListener listener);
}
