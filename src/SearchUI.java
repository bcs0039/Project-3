import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchUI {

    public JFrame view;

    public JButton btnSearch = new JButton("Search");

    public JTextField txtName = new JTextField(20);
    public JTextField txtMinPrice = new JTextField(20);
    public JTextField txtMaxPrice = new JTextField(20);

    UserModel user;

    public SearchUI(UserModel u) {

        user = u;

        this.view = new JFrame();

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Search Product Main Menu");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));


        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("Name "));
        line1.add(txtName);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Min Price "));
        line2.add(txtMinPrice);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Max Price "));
        line3.add(txtMaxPrice);
        view.getContentPane().add(line3);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnSearch);
        view.getContentPane().add(panelButtons);

        btnSearch.addActionListener(new SearchButtonListerner());

    }

    class SearchButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            String name = txtName.getText();
            double minPrice = Double.parseDouble(txtMinPrice.getText());
            double maxPrice = Double.parseDouble(txtMaxPrice.getText());

            ProductSearchUI ui = new ProductSearchUI(user, name, minPrice, maxPrice);
            ui.view.setVisible(true);

        }
    }

}
