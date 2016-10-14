package com.vica.ui;

import com.vica.app.Client;
import com.vica.bean.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Vica-tony on 2016/8/10.
 * The Client Form Of Socket Chat
 */
public class ClientForm {
    private JPanel panel1;
    private JPanel main;
    private JButton btn_start;
    private JTextField txt_host;
    private JTextField txt_port;
    private JButton btn_stop;
    private JList ls_user;
    private JTextArea txt_panel;
    private JTextField txt_msg;
    private JButton btn_send;

    private JFrame frame;
    private User user;
    private boolean connected = false;

    private Client client = null;

    public ClientForm() {
        frame = new JFrame();
        frame.setContentPane(main);
        frame.setResizable(false);

        frame.setTitle("客户端-离线");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                OnWindowClosing();
            }
        });
        RegisterEventListener();

        btn_start.setEnabled(true);
        btn_start.setText("连接");
        btn_stop.setEnabled(false);
        btn_send.setEnabled(false);

        ls_user.setSelectedIndex(0);
    }

    /**
     * Set The Title Of This Form
     * @param title
     */
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /**
     * Show this form
     */
    public void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Register All Events Listener
     */
    private void RegisterEventListener() {
        btn_start.addActionListener(e -> OnStart());
        btn_stop.addActionListener(e -> OnStop());
        btn_send.addActionListener(e -> OnSend());

    }

    /**
     * The Actions On Closing Form
     */
    private void OnWindowClosing() {
        CloseConnection();
    }

    /**
     * On Start
     */
    private void OnStart() {
        EnterClientDialog dialog = new EnterClientDialog();
        dialog.setOk(this::OnEnterOk);
        dialog.setCancle(this::OnEnterCancle);
        dialog.showDialog();
    }

    /**
     * On Stop
     */
    private void OnStop() {
        CloseConnection();
    }

    /**
     * On Send
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
        client.send(msg, to);
    }

    /**
     * open Connection
     */
    private void DoConnection() {
        if (client.Connection(user)) {
            connected = true;
            setTitle("客户端-"+user.getName()+"在线");
            btn_start.setText("已连接");
            btn_start.setEnabled(false);
            btn_stop.setEnabled(true);
            btn_send.setEnabled(true);
            client.start();
        } else {
            JOptionPane.showMessageDialog(frame, "用户信息冲突或链接已满", "失败", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * close connection
     */
    private void CloseConnection() {
        if (client != null && connected)
            client.close();
        else
            return;
        btn_start.setEnabled(true);
        btn_start.setText("连接");
        btn_stop.setEnabled(false);
        btn_send.setEnabled(false);
        setTitle("客户端-离线");
        connected = false;
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("*(All)");
        ls_user.setModel(model);
        ls_user.updateUI();
        ls_user.setSelectedIndex(0);
    }

    /**
     * login the user
     * @param name the username
     * @param pass the password
     */
    private void OnEnterOk(String name, String pass) {
        user = new User();
        user.setName(name);
        user.setPass(pass);
        try {
            String ip = txt_host.getText();
            int port = Integer.parseInt(txt_port.getText());
            if (ip.isEmpty() || port < 1024) {
                JOptionPane.showMessageDialog(frame, "输入信息不能为空或端口越界", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            client = new Client(ip, port);
            client.setDispaching(this::GetMessage);
            DoConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "链接信息非法", "警告", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void OnEnterCancle() {

    }

    /**
     * Read Message And Handle It, Such As Show Msg Or Alert Error
     * @param msg The Message
     * @param from Who Send This
     * @param to Who Received This
     */
    private void GetMessage(String msg, User from, User to) {
        if (from.getName().equals("success")) {
            ReloadClients(msg);
        } else if (from.getName().equals("error")) {
            JOptionPane.showMessageDialog(frame, msg);
        } else {
            txt_panel.setText(txt_panel.getText() + "\r\n[ " + from.getName() + " ] 说：" + msg);
        }
    }

    /**
     * Split Online Clients Info, Update The Online People
     * @param clients
     */
    private void ReloadClients(String clients) {
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("*(All)");
        for (String item : clients.split(",")) {
            if (!item.isEmpty()) {
                if (!item.substring(0, item.indexOf('(')).equals(user.getName()))
                    model.addElement(item);
            }
        }
        txt_panel.setText(txt_panel.getText() + "\r\n             =====[当前在线：" + (model.size()) + "人]=====");
        ls_user.setModel(model);
        ls_user.setSelectedIndex(0);
    }

//    public static void main(String[] args) {
//        new ClientForm().show();
//    }
}
