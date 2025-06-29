package app;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private LoginSuccessListener loginSuccessListener;

    public LoginForm() {
        setTitle("Form Login");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Nama Pengguna:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Kata Sandi:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Masuk");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);
        setVisible(true);

        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            User user = DBHandler.login(username, password);

            if (user != null) {
                if (loginSuccessListener != null) {
                    dispose(); // Tutup form login
                    loginSuccessListener.onLoginSuccess(user);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal. Periksa nama pengguna dan kata sandi.");
            }
        });
    }

    public void setLoginSuccessListener(LoginSuccessListener listener) {
        this.loginSuccessListener = listener;
    }
}
