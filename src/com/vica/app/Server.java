package com.vica.app;

import com.vica.bean.User;
import com.vica.core.GotMessageListener;
import com.vica.core.OnMessageDispaching;
import com.vica.core.TCPServerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务端主线程
 * Created by Vica-tony on 2016/8/7.
 */
public class Server extends Thread {
    List<TCPServerThread> clients;
    private int port;
    private int maxSize = 10;

    private boolean keepRunning = true;

    private OnMessageDispaching dispaching = null;

    //系统名称，用户不能注册的名称
    private List<String> keyName=new ArrayList<>();

    /**
     * The message listener, for package msg and handle it
     */
    private GotMessageListener listener = new GotMessageListener() {
        @Override
        public void Message(String msg, User from, User to) {
            if (from == null) {
                from = new User();
                from.setName("【服务器】");
            }
            if (to == null) {
                to = new User();
                to.setName("*");
            }
            for (TCPServerThread t : clients) {
                if (to.getName().equals("*")) {
                    if (!t.getUser().getName().equals(from.getName()))
                        t.Write("#" + from.getName() + ":" + msg);
                    continue;
                } else if (t.getUser().getName().equals(to.getName())) {
                    t.Write("#" + from.getName() + ":" + msg);
                } else {
                    continue;
                }
                break;
            }
            if (dispaching != null)
                dispaching.dispach(msg, from, to);
        }
    };

    /**
     * connection closing event handler, package msg and send to all clients
     */
    private TCPServerThread.OnClosing closing = new TCPServerThread.OnClosing() {
        @Override
        public void close(TCPServerThread thread) {
            String name = thread.getUser().getName();
            System.out.println("客户端退出："+name);
            clients.remove(thread);
            try {
                thread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(clients.size()>0) {
                User server = new User();
                server.setName("【服务器】");
                listener.Message(name+"离开了",server,null);
                User from = new User();
                from.setName("success");
                listener.Message(getClients(), from, null);
            }else{
                User from = new User();
                from.setName("success");
                dispaching.dispach(null,from,null);
            }
        }
    };

    /**
     * set the flag of thread for keep running
     * @param running
     */
    public void setKeepRunning(boolean running) {
        keepRunning = running;
    }

    public Server(int port, int max) {
        this.port = port;
        maxSize=max;
        clients = new ArrayList<>();
        keyName = new ArrayList<>();
        keyName.add("【服务器】");
        keyName.add("success");
        keyName.add("fail");
        keyName.add("error");
        keyName.add("close");
    }

    /**
     * set the msg dispatching handler
     * @param dispaching
     */
    public void setDispaching(OnMessageDispaching dispaching) {
        this.dispaching = dispaching;
    }

    /**
     * send message to client
     * @param msg the msg
     * @param to client
     */
    public void send(String msg, User to) {
        listener.Message(msg, null, to);
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);
            while (keepRunning) {
                Socket s = socket.accept();
                buildClient(s);
            }
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close server and close all client connection
     */
    private void close() {
        for(TCPServerThread t:clients){
            try {
                t.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        clients.clear();
    }

    /**
     * register a user thread connection
     * @param s the connection
     */
    private void buildClient(Socket s) {
        TCPServerThread cl = new TCPServerThread(s);
        for (TCPServerThread t : clients) {
            if (clients.size()>=maxSize || t.getUser().getName().equals(cl.getUser().getName()) || keyName.contains(cl.getUser().getName())) {
                cl.Write("#fail:");
                return;
            }
        }
        cl.setListener(listener);
        cl.setClosing(closing);
        cl.start();
        User server = new User();
        server.setName("【服务器】");
        listener.Message("欢迎"+cl.getUser().getName()+"加入",server,null);
        clients.add(cl);
        User from=new User();
        from.setName("success");
        listener.Message(getClients(),from,null);
    }

    /**
     * return the clients list connected
     * @return
     */
    private String getClients(){
        String list="";
        for(TCPServerThread t:clients){
            list+=t.getUser().getName()+"("+t.getUser().getIp()+"),";
        }
        return list.substring(0,list.length()-1);
    }

}
