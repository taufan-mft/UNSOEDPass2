package com.topanlabs.unsoedpass;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface mahaint {

    @GET("/mahasiswa2/{nim}/")
    Call<mahasis> getUser(@Path("nim") String nim, @Header("Authorization") String token);

    @POST("/mahasiswa/")
    Call<mahasis> createUser(@Body mahasis mahasis, @Header("Authorization") String token);
}