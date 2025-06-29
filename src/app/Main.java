package app;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        // 🔥 Set locale default ke Bahasa Indonesia
        Locale.setDefault(new Locale("id"));

        // Panggil login form
        LoginForm loginForm = new LoginForm();

        loginForm.setLoginSuccessListener(user -> {
            // Tampilkan GUI sesuai peran
            if (user.getRole().equals("admin")) {
                new PasienTableGUI(user.getUsername(), false);
            } else {
                new PasienTableGUI(user.getUsername(), true);
            }
        });
    }
}
