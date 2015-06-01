package com.levik.jdbc.configuration.model;

public class BasicDataSource {

    private String host;
    private String post;

    private String sid;
    private String login;
    private String password;

    public BasicDataSource(final String host, final String post,final String sid, final String login,final  String password) {
        this.host = host;
        this.post = post;
        this.sid = sid;
        this.login = login;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getPost() {
        return post;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getSid() {
        return sid;
    }
}
