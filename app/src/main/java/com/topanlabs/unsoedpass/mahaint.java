package com.topanlabs.unsoedpass;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface mahaint {

    @GET("/mahasiswa2/{nim}/")
    Call<mahasis> getUser(@Header("Authorization") String token, @Path("nim") String nim);

    @POST("/createmaha/")
    Call<mahasis> createUser(@Body mahasis mahasis);

    @GET("/mahasiswano/{nim}/")
    Call<Void> checkUser(@Path("nim") String nim);

    @PATCH("/updatemaha/{nim}/")
    Call<Void> gantiKodekel(@Path("nim") String nim, @Body mahasis mahasis, @Header("Authorization") String token);
}