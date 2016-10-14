package com.vica.ui;

import com.vica.app.Server;
import com.vica.bean.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Vica-tony on 2016/8/8.
 * 服务端窗体
 */
public class ServerForm {
    private JPanel main;
    private JButton btn_start;
    private JTextField txt_max;
    private JTextField txt_port;
    private JButton btn_stop;
    private JTextArea txt_panel;
    private JTextField txt_msg;
    private JButton btn_send;
    private JList ls_user;

    private JFrame frame;
    private Server server = null;

    public ServerForm() {
        frame = new JFrame("服务端");
        frame.setContentPane(main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        RegisterEvent();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                OnStop();
            }
        });
        btn_start.setText("启动");
        btn_start.setEnabled(true);
        btn_stop.setEnabled(false);
        btn_send.setEnabled(false);
    }

    /**
     * 显示窗体
     */
    public void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * 注册事件
     */
    private void RegisterEvent() {
        btn_start.addActionListener(e -> OnStart());
        btn_stop.addActionListener(e -> OnStop());
        btn_send.addActionListener(e -> OnSend());
    }

    /**
     * 启动事件
     */
    private void OnStart() {
        try {
            int port = Integer.parseInt(txt_port.getText());
            int max = Integer.parseInt(txt_max.getText());
            server = new Server(port, max);
            server.setDispaching(this::OnMessageReceived);
        } catch (Exception e) {
            server = null;
        }
        if (server != null) {
            server.start();
            frame.setTitle("服务端-已启动");
            btn_start.setText("已启动");
            btn_start.setEnabled(false);
            btn_stop.setEnabled(true);
            btn_send.setEnabled(true);
        }
    }

    /**
     * 停止事件
     */
    private void OnStop() {
        if (server != null) {
            server.setKeepRunning(false);
            btn_start.setText("启动");
            frame.setTitle("服务端");
            btn_start.setEnabled(true);
            btn_stop.setEnabled(false);
            btn_send.setEnabled(false);
        }
    }

    /**
     * 消息发送事件
     */
    private void OnSend() {
        String msg = txt_msg.getText();
        if (msg.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "消息不能为空");
            return;
        }
        txt_msg.setText("");
        String temp = ls_user.getSelectedValue().toString();
        String name = temp.substring(0, temp.indexOf('('));
        User to = new User();
        to.setName(name);
        server.send(msg, to);

    }

    /**
     * 消息接收事件
     * @param msg 消息体
     * @param from 来自（用户）
     * @param to 发给（用户）
     */
    private void OnMessageReceived(String msg, User from, User to) {
        if (from.getName().equals("success")) {
            DefaultListModel<String> model = new DefaultListModel<>();
            model.addElement("*(All)");
            if (msg != null)
                for (String item : msg.split(",")) {
                    if (!item.isEmpty()) {
                        model.addElement(item);
                    }
                }
            this.ls_user.setModel(model);
            this.ls_user.setSelectedIndex(0);
        } else {
            txt_panel.setText(txt_panel.getText() + "\r\n[ " + from.getName() + " ] 对 [ " + to.getName() + " ] 说：" + msg);
        }
    }


//    public static void main(String[] args) {
//        ServerForm serverForm = new ServerForm();
//        serverForm.show();
//    }
}
