package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TindakanForm extends JFrame {
    public TindakanForm(int pasienId, String pasienNama, String dokter) {
        setTitle("Riwayat Tindakan - " + pasienNama);
        setSize(500, 350);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Tanggal", "Deskripsi", "Dokter"}, 0);
        List<String[]> tindakanList = DBHandler.getTindakanByPasien(pasienId);
        for (String[] row : tindakanList) {
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JTextField tglField = new JTextField(10);
        JTextField descField = new JTextField(20);
        JTextField dokterField = new JTextField(15);
        JButton addBtn = new JButton("Tambah");
        JButton backBtn = new JButton("Kembali");

        addBtn.addActionListener(e -> {
            String tgl = tglField.getText();
            String desc = descField.getText();
            String dokterInput = dokterField.getText();
            DBHandler.tambahTindakan(pasienId, tgl, desc, dokterInput);
            model.addRow(new String[]{tgl, desc, dokterInput});
        });

        backBtn.addActionListener(e -> {
            dispose();
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Tanggal (yyyy-mm-dd):")); inputPanel.add(tglField);
        inputPanel.add(new JLabel("Deskripsi:")); inputPanel.add(descField);
        inputPanel.add(new JLabel("Dokter:")); inputPanel.add(dokterField);
        inputPanel.add(addBtn);
        inputPanel.add(backBtn);

        add(inputPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}
