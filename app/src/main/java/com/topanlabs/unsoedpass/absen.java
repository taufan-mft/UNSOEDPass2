package com.topanlabs.unsoedpass;

public class absen {
    private String namatkul;
    private String absen;
    private String totpertemuan;
    private String jam;

    public absen(String namatkul, String absen, String totpertemuan) {
        this.namatkul = namatkul;
        this.absen = absen;
        this.totpertemuan = totpertemuan;

    }

    public String getNamatkul() {
        return namatkul;
    }

    public void setNamatkul(String namatkul) {
        this.namatkul = namatkul;
    }

    public String getAbsen() {
        return absen;
    }

    public void setAbsen(String hari) {
        this.absen = absen;
    }

    public String getTotpertemuan() {
        return totpertemuan;
    }

    public void setTotpertemuan(String dosen) {
        this.totpertemuan = totpertemuan;
    }

}
