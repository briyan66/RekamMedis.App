package app;

import java.io.Serializable;

public class Pasien<T extends Serializable> implements Serializable {
    private int id;
    private String nama;
    private T nik;

    public Pasien(String nama, T nik) {
        this.nama = nama;
        this.nik = nik;
    }

    public Pasien(int id, String nama, T nik) {
        this.id = id;
        this.nama = nama;
        this.nik = nik;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public T getNik() { return nik; }
    public void setNik(T nik) { this.nik = nik; }
}
