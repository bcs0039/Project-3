import com.google.gson.Gson;

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

public class ChangeUserTypeUI {

    public JFrame view;

    public JButton btnChangeType = new JButton("Change Type");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtUsername = new JTextField(10);
    public JTextField txtNewType = new JTextField(10);

    public ChangeUserTypeUI() {

        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Change User Type");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line = new JPanel(new FlowLayout());
        line.add(new JLabel("Username "));
        line.add(txtUsername);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(new JLabel("New Type "));
        line.add(txtNewType);
        view.getContentPane().add(line);

        line = new JPanel(new FlowLayout());
        line.add(btnChangeType);
        line.add(btnCancel);
        view.getContentPane().add(line);

        btnChangeType.addActionListener(new ChangeTypeButtonListerner());

    }

    public void run() {
        view.setVisible(true);
    }

    private class ChangeTypeButtonListerner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            UserModel user = new UserModel();

            user.mUsername = txtUsername.getText();
            user.mPassword = "";
            user.mFullname = "";
            user.mUserType = Integer.parseInt(txtNewType.getText());

            if (user.mUsername.length() == 0 ) {
                JOptionPane.showMessageDialog(null, "Username cannot be null!");
                return;
            }

            if (user.mUserType != 0 && user.mUserType != 1 && user.mUserType != 2 &&user.mUserType != 3) {
                JOptionPane.showMessageDialog(null, "User type is invalid!");
                return;
            }

            Gson gson = new Gson();

            MessageModel msg = new MessageModel();
            msg.code = MessageModel.CHANGE_USER_TYPE;
            msg.data = gson.toJson(user);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED)
                JOptionPane.showMessageDialog(null, "Failed to change user type!");
            else {

                JOptionPane.showMessageDialog(null, "Succeeded at changing user type!");
                view.setVisible(false);

            }

        }
    }

}
