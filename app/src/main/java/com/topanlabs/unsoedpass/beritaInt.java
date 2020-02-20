package com.topanlabs.unsoedpass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface beritaInt {

    @GET("/berita/")
    Call<List<beritaModel>> getBerita(@Header("Authorization") String token);

    @GET("/eventall/")
    Call<List<beritaModel>> getEvent();

    @GET("/pengumumanall/")
    Call<List<beritaModel>> getPengumuman();

    @GET("/beasiswaall/")
    Call<List<beritaModel>> getBeasiswa();
}
