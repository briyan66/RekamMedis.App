package app;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class PasienTableGUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private String dokter;

    public PasienTableGUI(String dokter, boolean isDokter) {
        this.dokter = dokter;

        setTitle("Daftar Pasien");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Cari");
        JButton refreshButton = new JButton("Refresh");
        JButton addButton = new JButton("Tambah");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");
        JButton tindakanButton = new JButton("Lihat Tindakan");

        // Nonaktifkan tombol jika user adalah dokter
        addButton.setEnabled(!isDokter);
        editButton.setEnabled(!isDokter);
        deleteButton.setEnabled(!isDokter);

        topPanel.add(new JLabel("Cari Nama:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(tindakanButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama", "NIK"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            loadPasienData(keyword);
        });

        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadPasienData("");
        });

        addButton.addActionListener(e -> {
            JTextField namaField = new JTextField();
            JTextField nikField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Nama:"));
            panel.add(namaField);
            panel.add(new JLabel("NIK:"));
            panel.add(nikField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Tambah Pasien", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String nama = namaField.getText().trim();
                String nik = nikField.getText().trim();
                if (!nama.isEmpty() && !nik.isEmpty()) {
                    String nikEncrypted = EnkripsiUtil.enkripsi(nik);
                    DBHandler.insertPasien(nama, nikEncrypted);
                    loadPasienData("");
                }
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                String currentNama = tableModel.getValueAt(selectedRow, 1).toString();
                String currentNIK = tableModel.getValueAt(selectedRow, 2).toString();

                JTextField namaField = new JTextField(currentNama);
                JTextField nikField = new JTextField(currentNIK);
                JPanel panel = new JPanel(new GridLayout(2, 2));
                panel.add(new JLabel("Nama:"));
                panel.add(namaField);
                panel.add(new JLabel("NIK:"));
                panel.add(nikField);

                int result = JOptionPane.showConfirmDialog(this, panel, "Edit Pasien", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String newNama = namaField.getText().trim();
                    String newNIK = nikField.getText().trim();
                    if (!newNama.isEmpty() && !newNIK.isEmpty()) {
                        String newNIKEncrypted = EnkripsiUtil.enkripsi(newNIK);
                        DBHandler.updatePasien(id, newNama, newNIKEncrypted);
                        loadPasienData("");
                    }
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus pasien?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    DBHandler.deletePasien(id);
                    loadPasienData("");
                }
            }
        });

        tindakanButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                String nama = tableModel.getValueAt(selectedRow, 1).toString();
                new TindakanForm(id, nama, dokter);
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih pasien terlebih dahulu.");
            }
        });

        loadPasienData("");
        setVisible(true);
    }

    private void loadPasienData(String keyword) {
        tableModel.setRowCount(0);
        List<Pasien<String>> pasienList = DBHandler.getPasienList(keyword);
        for (Pasien<String> p : pasienList) {
            String nikAsli = EnkripsiUtil.deskripsi(p.getNik());
            tableModel.addRow(new Object[]{p.getId(), p.getNama(), nikAsli});
        }
    }
}
