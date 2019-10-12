package com.topanlabs.unsoedpass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class beritaModel {

    @SerializedName("headline")
    @Expose
    private String headline;
    @SerializedName("konten")
    @Expose
    private String konten;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("author")
    @Expose
    private String author;

    public beritaModel(String headline, String konten, String cover, String author) {
        this.headline = headline;
        this.konten = konten;
        this.cover = cover;
        this.author = author;

    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getKonten() {
        return konten;
    }

    public void setKonten(String konten) {
        this.konten = konten;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
