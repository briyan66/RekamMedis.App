package app;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.concurrent.*;

public class RekamMedisGUI extends JFrame {
    public RekamMedisGUI(ResourceBundle bundle) {
        setTitle(bundle.getString("title"));
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lblNama = new JLabel(bundle.getString("inputName"));
        JTextField txtNama = new JTextField(20);
        JLabel lblNIK = new JLabel(bundle.getString("inputId"));
        JTextField txtNIK = new JTextField(20);
        JButton btnSimpan = new JButton(bundle.getString("saveBtn"));

        btnSimpan.addActionListener(e -> {
            String nama = txtNama.getText();
            String nik = EnkripsiUtil.enkripsi(txtNIK.getText());
            Pasien<String> pasien = new Pasien<>(nama, nik);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                DBHandler.insertPasien(pasien.getNama(), pasien.getNik());
                FileHandler.simpan(pasien);
                JOptionPane.showMessageDialog(this, bundle.getString("saveSuccess"));
            });
            executor.shutdown();
        });

        setLayout(new GridLayout(3, 2));
        add(lblNama); add(txtNama);
        add(lblNIK); add(txtNIK);
        add(new JLabel()); add(btnSimpan);

        setVisible(true);
    }
}