package com.vica.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by Vica-tony on 2016/8/7.
 * the base form, abandoned form class
 */
@Deprecated
public class MyBaseFrame extends JFrame {

    private boolean isRunning = true;

    private JButton btn_start;
    private JButton btn_stop;
    private JButton btn_send;

    private JPanel pl_left;
    private JPanel pl_main;
    private JPanel pl_main_top;
    private JPanel pl_main_bottom;

    private JLabel lb_left_host;
    private JLabel lb_left_port;
    private JLabel lb_left_state;

    private JTextArea txt_main;
    private JTextField txt_host;
    private JTextField txt_port;
    private JTextField txt_msg;

    /**
     * Create instance by title
     * @param title
     */
    public MyBaseFrame(String title) {
        this.setTitle(title);
        BuildLayout();
        this.setLocationRelativeTo(null);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    }

    /**
     * Closing Window Event
     * @param e
     */
    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (isRunning) {
                if (JOptionPane.showConfirmDialog(this, "test info", "警告", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    close();
                } else {
                    return;
                }

            } else {
                super.processWindowEvent(e);
            }
        }
    }

    /**
     * the layout build (too hard to abandon this)
     */
    private void BuildLayout() {
        setSize(600, 450);
        this.setLayout(null);
        this.setResizable(false);

        pl_left = new JPanel(true);
        pl_left.setLocation(0, 0);
        pl_left.setSize(150, 410);
        pl_left.setBackground(Color.darkGray);
        pl_left.setLayout(null);

        lb_left_host = new JLabel("主机");
        lb_left_host.setBounds(5, 20, 130, 30);
        lb_left_host.setForeground(Color.WHITE);
        lb_left_port = new JLabel("端口");
        lb_left_port.setBounds(5, 100, 130, 30);
        lb_left_port.setForeground(Color.WHITE);
        lb_left_state = new JLabel("未链接");
        lb_left_state.setBounds(10, 280, 130, 30);
        lb_left_state.setForeground(Color.RED);

        txt_host = new JTextField();
        txt_host.setBounds(5, 60, 130, 30);
        txt_host.setBackground(Color.LIGHT_GRAY);
        txt_host.setForeground(Color.WHITE);
        txt_host.setBorder(null);

        txt_port = new JTextField();
        txt_port.setBounds(5, 140, 130, 30);
        txt_port.setBackground(Color.LIGHT_GRAY);
        txt_port.setForeground(Color.WHITE);
        txt_port.setBorder(null);

        btn_start = new JButton("链接");
        btn_start.setBounds(5, 200, 130, 30);
        btn_start.setBorder(null);
        btn_stop = new JButton("断开");
        btn_stop.setBounds(5, 240, 130, 30);
        btn_stop.setBorder(null);
        btn_stop.setEnabled(false);

        pl_left.add(lb_left_host);
        pl_left.add(txt_host);
        pl_left.add(lb_left_port);
        pl_left.add(txt_port);
        pl_left.add(btn_start);
        pl_left.add(btn_stop);

        pl_left.add(lb_left_state);

        add(pl_left);

        pl_main = new JPanel();
        pl_main.setSize(428, 410);
        pl_main.setLocation(152, 0);
        pl_main.setBackground(Color.DARK_GRAY);
        pl_main.setLayout(null);

        pl_main_top = new JPanel();
        pl_main_top.setBounds(0, 0, 428, 360);
        pl_main_top.setBorder(new TitledBorder("消息"));
        pl_main_top.setLayout(new BoxLayout(pl_main_top, BoxLayout.Y_AXIS));

        txt_main = new JTextArea();
        txt_main.setEditable(false);
        txt_msg = new JTextField();

        pl_main_top.add(txt_main);

        pl_main_bottom = new JPanel();
        pl_main_bottom.setBounds(0, 360, 428, 50);
        pl_main_bottom.setBorder(new TitledBorder("输入"));
        pl_main_bottom.setLayout(new BoxLayout(pl_main_bottom, BoxLayout.X_AXIS));

        pl_main_bottom.add(txt_msg);

        btn_send = new JButton("发送");

        pl_main_bottom.add(btn_send);

        pl_main.add(pl_main_top);
        pl_main.add(pl_main_bottom);

        add(pl_main);

    }

    public void close(){
        this.dispose();
    }

    public void setStartText(String txt) {
        btn_start.setText(txt);
    }

    public void setStopText(String txt) {
        btn_stop.setText(txt);
    }

    public void setHostPortEnable(boolean host, boolean port) {
        txt_host.setEnabled(host);
        txt_port.setEnabled(port);
    }

    public void setStartListened(ActionListener action) {
        btn_start.addActionListener(action);
    }

    public void setStopListened(ActionListener action) {
        btn_stop.addActionListener(action);
    }

    public void setSendListened(ActionListener action) {
        btn_send.addActionListener(action);
    }

    public void setMsgListened(ActionListener action) {
        txt_msg.addActionListener(action);
    }

    public void setStateText(String txt) {
        lb_left_state.setText(txt);
    }
}
