package com.vica.core;

import com.vica.bean.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Vica-tony on 2016/8/7.
 * TCP server connection thread
 */
public class TCPServerThread extends Thread {

    private User user;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private GotMessageListener listener;
    private boolean keepWorking = true;
    private OnClosing closing =null;

    /**
     * set the flag of thread for keep running
     * @param working
     */
    public void setKeepWorking(boolean working){
        keepWorking=working;
    }

    /**
     * get the user of this thread serviced
     * @return
     */
    public User getUser(){
        return user;
    }

    /**
     * get the reader
     * @return
     */
    public BufferedReader getReader(){
        return reader;
    }

    /**
     * get the writer
     * @return
     */
    public PrintWriter getWriter(){
        return writer;
    }

    /**
     * set the message received handler
     * @param listener
     */
    public void setListener(GotMessageListener listener){
        this.listener=listener;
    }


    public TCPServerThread(Socket socket){
        this.socket=socket;
        try {
            reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new PrintWriter(socket.getOutputStream());

            String info = reader.readLine();
            String name = info.substring(0,info.indexOf(':')).substring(1);
            String pass = info.substring(name.length()+2);
            this.user=new User();
            user.setName(name);
            user.setPass(pass);
            user.setIp(socket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write a msg into the writer
     * @param msg
     */
    public void Write(String msg){
        writer.println(msg);
        writer.flush();
    }

    @Override
    public void run() {
        while(keepWorking){
            User toUser = null;
            String line = null;
            try {
                line = reader.readLine();
                System.out.println("服务器收到="+line);
                if(line.startsWith("#")) {
                    String prefix = line.substring(0, line.indexOf(':'));
                    String name=prefix.substring(1);
                    if(name.equals("close")){
                        closing.close(this);
                        return;
                    }
                    toUser=new User();
                    toUser.setName(name);
                    line=line.substring(name.length()+2);
                }else{
                    writer.println("#error:非法消息");
                    writer.flush();
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            listener.Message(line,user,toUser);
        }
    }

    /**
     * set the closing event action
     * @param closing
     */
    public void setClosing(OnClosing closing){
        this.closing=closing;
    }

    /**
     * close the connection
     * @throws IOException
     */
    public void close() throws IOException {
        keepWorking = false;
        writer.close();
        reader.close();
        socket.close();
    }

    public interface OnClosing{
        public void close(TCPServerThread thread);
    }
}
