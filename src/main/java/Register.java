import database.User;
import database.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * The Register class represents the user interface for the registration functionality in the WalletWise application.
 */
public class Register extends JFrame {
    private JPanel Register;
    private JPanel Left;
    private JLabel ImageLogo;
    private JPanel Right;
    private JTextField emailTextField;
    private JPasswordField password;
    private JPasswordField confirmPassword;
    private JButton registerButton1;
    private JButton loginButton;
    private JTextField nick;

    /**
     * Constructs a new Register frame with all its components and initializes event listeners.
     */
    public Register() {
        setContentPane(Register);
        setTitle("WalletWise");
        setSize(1000, 600);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Register.this.dispose();
                new Login();
            }
        });

        registerButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegister();
            }
        });

        confirmPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // If Enter key is pressed, perform login action
                    performRegister();
                }
            }
        });
    }

    /**
     * Performs the registration action by validating user input, checking constraints, and creating a new account.
     */
    private void performRegister() {
        String regexPatternForEmail = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        String regexPatternForPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
        Pattern patternEmail = Pattern.compile(regexPatternForEmail);
        Pattern patternPass = Pattern.compile(regexPatternForPassword);

        if (nick.getText().length() < 3) {
            JOptionPane.showMessageDialog(Register.this, "Nickname-ul trebuie sa aiba minim 3 caractere");
        } else if (!patternEmail.matcher(emailTextField.getText()).matches()) {
            JOptionPane.showMessageDialog(Register.this, "Email-ul nu este valid");
        } else if (String.valueOf(password.getPassword()).length() < 3) {
            JOptionPane.showMessageDialog(Register.this, "Parola trebuie sa aiba minim 3 caractere");
        } else if (!patternPass.matcher(String.valueOf(password.getPassword())).matches()) {
            JOptionPane.showMessageDialog(Register.this, "Parola trebuie sa contina: \n" +
                    "• Intre 8 si 20 de caractere \n" +
                    "• Cel putin o cifra\n" +
                    "• Cel putin o litera mica\n" +
                    "• Cel putin o litera mare\n" +
                    "!!!Parola nu poate contine spatii");
        } else if (!(Arrays.equals(password.getPassword(), confirmPassword.getPassword()))) {
            JOptionPane.showMessageDialog(Register.this, "Parolele nu se potrivesc");
        } else {
            String nickname = nick.getText();
            String email = emailTextField.getText();
            String pass = String.valueOf(password.getPassword());
            int result = jdbc.register(nickname, email, pass);
            switch (result) {
                case 1:
                    JOptionPane.showMessageDialog(Register.this, "Contul a fost creat cu succes");
                    Register.this.dispose();
                    new Login();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(Register.this, "Nickname-ul este luat");
                    break;
                case 3:
                    JOptionPane.showMessageDialog(Register.this, "Email-ul este luat");
                    break;
                default:
                    JOptionPane.showMessageDialog(Register.this, "Eroare la crearea contului");
            }
        }
    }

    /**
     * Custom component creation method for setting up the application logo.
     */
    private void createUIComponents() {
        ImageIcon imageIcon = new ImageIcon("src/icon/logo.png");
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(300, 200, java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(newimg);
        ImageLogo = new JLabel(icon);
    }
}
