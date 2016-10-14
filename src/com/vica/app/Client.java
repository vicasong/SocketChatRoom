package com.vica.app;

import com.vica.bean.User;
import com.vica.core.OnMessageDispaching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Vica-tony on 2016/8/7.
 * The Client Thread
 */
public class Client extends Thread {

    private Socket socket;

    private String host;
    private int port;
    private OnMessageDispaching dispaching = null;
    private User user;

    private BufferedReader reader = null;
    private PrintWriter writer = null;
    private boolean keepRunning = true;

    public Client(String ip, int port) {
        host = ip;
        this.port = port;
    }

    /**
     * Connection the server use login user
     * @param user the login user
     * @return connection is ok
     */
    public boolean Connection(User user) {
        this.user = user;
        try {
            System.out.println(host+":"+port);
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            String auth = "#" + user.getName() + ":" + user.getPass();
            writer.println(auth);
            writer.flush();
            String result = reader.readLine();
            System.out.println("验证请求=" + result);
            if (result.startsWith("#success:")) {
                String from = result.substring(1, result.indexOf(':'));
                String msg = result.substring(from.length() + 2);
                User f = new User();
                f.setName(from);
                if (dispaching != null) {
                    dispaching.dispach(msg, f, user);
                }
                return true;
            } else {
                reader.close();
                writer.close();
                socket.close();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Send a message
     * @param msg the message
     * @param to who the msg be received
     */
    public void send(String msg, User to){
        String info = "#"+to.getName()+":"+msg;
        writer.println(info);
        writer.flush();
        dispaching.dispach(msg,user,to);
    }

    /**
     * close connection
     */
    public void close(){
        writer.println("#close:");
        writer.flush();
    }

    @Override
    public void run() {
        while (keepRunning) {
            try {
                String info = reader.readLine();
                if(info==null)
                    continue;
                System.out.println("客户端收到="+info);
                if (info.startsWith("#")) {
                    String from = info.substring(1, info.indexOf(':'));
                    String msg = info.substring(from.length() + 2);
                    User f = new User();
                    f.setName(from);
                    if (dispaching != null) {
                        dispaching.dispach(msg, f, user);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * set the msg dispaching handler
     * @param dispaching
     */
    public void setDispaching(OnMessageDispaching dispaching) {
        this.dispaching = dispaching;
    }
}
