package com.vica.core;

import com.vica.bean.User;

/**
 * Created by Vica-tony on 2016/8/7.
 * handler of message received
 */
public interface GotMessageListener {

    public void Message(String msg, User from, User to);
}
