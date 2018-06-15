package ru.otus.l16.messages;


import ru.otus.l16.app.Msg;

/**
 * Created by tully.
 */
public class MsgGetUserId extends Msg {

    private final String login;
    private final String password;

    public MsgGetUserId(String login, String password) {
        super(MsgGetUserId.class);
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
