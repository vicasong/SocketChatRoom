package com.vica.core;

import com.vica.bean.User;

/**
 * Created by Vica-tony on 2016/8/10.
 * dispatching handler of message
 */
public interface OnMessageDispaching {
    public void dispach(String msg, User from, User to);
}
