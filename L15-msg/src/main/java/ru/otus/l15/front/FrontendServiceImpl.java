package ru.otus.l15.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.app.CacheInfo;
import ru.otus.l15.app.CacheInfoListener;
import ru.otus.l15.app.LoginProcessListener;
import ru.otus.l15.app.message.MsgGetCacheInfo;
import ru.otus.l15.app.message.MsgGetUserId;
import ru.otus.l15.app.message.MsgSetCacheCapacity;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Message;
import ru.otus.l15.messageSystem.MessageSystem;
import ru.otus.l15.messageSystem.MessageSystemContext;

public class FrontendServiceImpl implements FrontendService {

    private final Address address;
    private final MessageSystemContext context;

    private LoginProcessListener loginProcessListener;

    private Logger log = LoggerFactory.getLogger(FrontendServiceImpl.class);

    public FrontendServiceImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
    }

    @Override
    public void init() {
        log.trace("init()");
        log.trace("Address: {}", address.getId());
        context.setFrontAddress(address);
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public void loginUser(String login, String password, LoginProcessListener listener) {
        log.trace("loginUser: {}, {}, {}", login, password, loginProcessListener);
        this.loginProcessListener = listener;
        Message message = new MsgGetUserId(getAddress(), context.getDbAddress(), login, password);
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void onUserLogged() {
        log.trace("onUserLogged()");
        if (loginProcessListener != null) {
            loginProcessListener.userLogged();
        }
    }

    @Override
    public void onWrongUser() {
        log.trace("onWrongUser()");
        if (loginProcessListener != null) {
            loginProcessListener.wrongUser();
        }
    }

    @Override
    public void setCacheCapacity(int capacity) {
        Message message = new MsgSetCacheCapacity(getAddress(), context.getCacheAddress(), capacity);
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void getCacheInfo(CacheInfoListener listener) {
        log.trace("getCacheInfo");
        Message message = new MsgGetCacheInfo(getAddress(), context.getCacheAddress(), listener);
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void onCacheInfo(CacheInfo info, CacheInfoListener listener) {
        if (listener != null) {
            listener.onCacheInfo(info);
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}
