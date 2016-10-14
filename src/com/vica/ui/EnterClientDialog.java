package com.vica.ui;

import javax.swing.*;
import java.awt.event.*;

/**
 * 登入对话框
 */
public class EnterClientDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane txtPane;
    private JTextField txt_name;
    private JPasswordField txt_pass;

    private OnCancleListener onCancleListener=null;
    private OnOkListener onOkListener=null;

    public EnterClientDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle("通信许可");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setResizable(false);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setTheTitle(String title){
        setTitle(title);
    }

    /**
     * 设置取消事件
     * @param cancleListener
     */
    public void setCancle(OnCancleListener cancleListener){
        onCancleListener=cancleListener;
    }

    /**
     * 设置确认事件
     * @param okListener
     */
    public void setOk(OnOkListener okListener){
        onOkListener=okListener;
    }

    private void onOK() {
        String name=txt_name.getText();
        String pass=new String(txt_pass.getPassword());
        if(onOkListener!=null){
            onOkListener.onOk(name,pass);
        }
        dispose();
    }

    private void onCancel() {
        if(onCancleListener!=null){
            onCancleListener.onCancle();
        }
        dispose();
    }

//    public static void main(String[] args) {
//        EnterClientDialog dialog = new EnterClientDialog();
//        dialog.showDialog();
//        System.exit(0);
//    }

    /**
     * 显示对话框
     */
    public void showDialog(){
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public interface OnCancleListener{
        public void onCancle();
    }
    public interface OnOkListener{
        public void onOk(String name, String pass);
    }
}
