package ru.otus.l15.front;

import ru.otus.l15.app.CacheInfo;
import ru.otus.l15.app.CacheInfoListener;
import ru.otus.l15.messageSystem.Addressee;
import ru.otus.l15.app.LoginProcessListener;

public interface FrontendService extends Addressee {

    void init();

    void loginUser(String login, String password, LoginProcessListener service);

    void onUserLogged();

    void onWrongUser();

    void setCacheCapacity(int capacity);

    void getCacheInfo(CacheInfoListener listener);

    void onCacheInfo(CacheInfo info, CacheInfoListener listener);
}
