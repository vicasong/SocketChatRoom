package com.vica.bean;

/**
 * Created by Vica-tony on 2016/8/7.
 * the client user
 */
public class User {

    private String name;
    private String ip;
    private String pass;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
