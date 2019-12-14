import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

public class ChangeCredentialsUI {

    public JFrame view;

    public JButton btnConfirm = new JButton("Confirm");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtNewPassword = new JTextField(10);
    public JTextField txtNewName = new JTextField(10);

    UserModel oldUser;

    public ChangeCredentialsUI(UserModel user) {

        oldUser = user;

        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Change Credentials");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line = new JPanel(new FlowLayout());
        line.add(new JLabel("New Password "));
        line.add(txtNewPassword);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("New Name "));
        line.add(txtNewName);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(btnConfirm);
        line.add(btnCancel);
        view.getContentPane().add(line);

        btnConfirm.addActionListener(new ConfirmButtonListerner());

    }

    public void run() {
        view.setVisible(true);
    }

    private class ConfirmButtonListerner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            UserModel newUser = new UserModel();

            newUser.mUsername = oldUser.mUsername;
            newUser.mPassword = txtNewPassword.getText();
            newUser.mFullname = txtNewName.getText();
            newUser.mUserType = oldUser.mUserType;

            if (oldUser.mUsername.length() == 0 || oldUser.mPassword.length() == 0) {
                JOptionPane.showMessageDialog(null, "Old Username or password cannot be null!");
                return;
            }

            if (newUser.mUsername.length() == 0 || newUser.mPassword.length() == 0) {
                JOptionPane.showMessageDialog(null, "Old Username or password cannot be null!");
                return;
            }

            Gson gson = new Gson();

            MessageModel msg = new MessageModel();
            msg.code = MessageModel.CHANGE_CREDENTIALS;
            msg.data = gson.toJson(oldUser);
            msg.data2 = gson.toJson(newUser);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED)
                JOptionPane.showMessageDialog(null, "Credentials changed failed!");
            else {

                JOptionPane.showMessageDialog(null, "Credentials changed succeeded!");
                view.setVisible(false);

            }

        }
    }

}
