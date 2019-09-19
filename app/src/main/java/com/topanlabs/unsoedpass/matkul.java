package com.topanlabs.unsoedpass;

public class matkul {

    private String namatkul;
    private String hari;
    private String dosen;
    private String jam;

    public matkul(String namatkul, String hari, String dosen, String jam) {
        this.namatkul = namatkul;
        this.hari = hari;
        this.dosen = dosen;
        this.jam = jam;

    }

    public String getNamatkul() {
        return namatkul;
    }

    public void setNamatkul(String namatkul) {
        this.namatkul = namatkul;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
