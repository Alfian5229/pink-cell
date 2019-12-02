package com.alfianwibowo.pinkcell;

public class AdapterItems {

    private String id;
    private String merk;
    private String type;
    private String ram;
    private String internal;
    private String kelengkapan;
    private String jaringan;
    private String deskripsi;
    private String harga_jual;
    private String harga_jual_maks;

    AdapterItems(String id, String merk, String type, String ram, String internal, String kelengkapan, String jaringan, String deskripsi, String harga_jual, String harga_jual_maks) {
        this.id = id;
        this.merk = merk;
        this.type = type;
        this.ram = ram;
        this.internal = internal;
        this.kelengkapan = kelengkapan;
        this.jaringan = jaringan;
        this.deskripsi = deskripsi;
        this.harga_jual = harga_jual;
        this.harga_jual_maks = harga_jual_maks;
    }

    public String getId() {
        return id;
    }

    public String getMerk() {
        return merk;
    }

    public String getType() {
        return type;
    }

    public String getRam() {
        return ram;
    }

    public String getInternal() {
        return internal;
    }

    public String getKelengkapan() {
        return kelengkapan;
    }

    public String getJaringan() {
        return jaringan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public String getHarga_jual_maks() {
        return harga_jual_maks;
    }
}
