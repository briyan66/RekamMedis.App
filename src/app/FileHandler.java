package app;

import java.io.*;

public class FileHandler {

    // Fungsi simpan data Pasien<T> ke file
    public static <T extends Serializable> void simpan(Pasien<T> pasien) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("backup.dat"))) {
            out.writeObject(pasien);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fungsi muat data Pasien<T> dari file
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> Pasien<T> muat() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("backup.dat"))) {
            return (Pasien<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
