import database.Account;
import database.User;
import database.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Login extends JFrame {
    private JPanel Left;
    private JPanel Right;
    private JPanel Login;
    private JTextField emailTextField;
    private JPasswordField passwordPasswordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel ImageLogo;

    public Login() {
        setContentPane(Login);
        setTitle("WalletWise");
        setSize(1000, 600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Login.this.dispose();
                new Register();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        passwordPasswordField.addKeyListener(new KeyAdapter() {
            @Override

            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // If Enter key is pressed, perform login action
                    performLogin();
                }
            }
        });
    }

    private void performLogin() {
        String email = emailTextField.getText();
        String pass = String.valueOf(passwordPasswordField.getPassword());
        User user = jdbc.validateLogin(email, pass);
        if (user != null) {
            ArrayList<Account> accounts = jdbc.getAccountsByUserId(user.getUser_id());
            ImageIcon imageIcon = new ImageIcon("src/icon/login.jpg");
            Image image = imageIcon.getImage();
            Image newimg = image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(newimg);
            JOptionPane.showMessageDialog(Login.this, "Welcome " + user.getNickname(), "👍", JOptionPane.PLAIN_MESSAGE, icon);
            Login.this.dispose();
            new Home(user, accounts);
        } else {
            JOptionPane.showMessageDialog(Login.this, "Datele nu corespund sau nu exista contul");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    jdbc.createSchemaIfNotExist();
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        ImageIcon imageIcon = new ImageIcon("src/icon/logo.png");
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(300, 200, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        ImageIcon icon = new ImageIcon(newimg);
        ImageLogo = new JLabel(icon);
//        ImageLogo.setIcon(icon);
//        ImageLogo.setIcon(img);
    }
}