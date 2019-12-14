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

public class AddUserUI {

    public JFrame view;

    public JButton btnAdd = new JButton("Add");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtUsername = new JTextField(10);
    public JTextField txtPassword = new JTextField(10);
    public JTextField txtFullname = new JTextField(10);
    public JTextField txtUserType = new JTextField(10);

    public AddUserUI() {

        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Add User");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line = new JPanel(new FlowLayout());
        line.add(new JLabel("Username "));
        line.add(txtUsername);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("Password "));
        line.add(txtPassword);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("Full Name "));
        line.add(txtFullname);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("User Type "));
        line.add(txtUserType);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(btnAdd);
        line.add(btnCancel);
        view.getContentPane().add(line);

        btnAdd.addActionListener(new AddButtonListerner());

    }

    public void run() {
        view.setVisible(true);
    }

    private class AddButtonListerner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            UserModel user = new UserModel();

            user.mUsername = txtUsername.getText();
            user.mPassword = txtPassword.getText();
            user.mFullname = txtFullname.getText();
            user.mUserType = Integer.parseInt(txtUserType.getText());

            if (user.mUsername.length() == 0 || user.mPassword.length() == 0 || user.mFullname.length() == 0) {
                JOptionPane.showMessageDialog(null, "Old Username, password, or full name cannot be null!");
                return;
            }

            if (user.mUserType != 0 && user.mUserType != 1 && user.mUserType != 2 &&user.mUserType != 3) {
                JOptionPane.showMessageDialog(null, "User type is invalid!");
                return;
            }

            Gson gson = new Gson();

            MessageModel msg = new MessageModel();
            msg.code = MessageModel.PUT_USER;
            msg.data = gson.toJson(user);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED)
                JOptionPane.showMessageDialog(null, "Failed to add user!");
            else {

                JOptionPane.showMessageDialog(null, "Succeeded at adding user!");
                view.setVisible(false);

            }

        }
    }

}
