package com.topanlabs.unsoedpass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface kelasInt {

    @GET("/retkelas/{nim}/")
    Call<List<kelasModel>> getKelas(@Header("Authorization") String token, @Path("nim") String nim);

    @GET("/liatkelas/{kodekelas}/")
    Call<kelasModel> cariKelas(@Header("Authorization")String token, @Path("kodekelas") String kodekelas);

    @POST("/createkelas/")
    Call<kelasModel> buatKelas(@Header("Authorization")String token, @Body kelasModel mahasis);

    @POST("/creatematkul/")
    Call<kelasModel> buatMatkul(@Header("Authorization")String token, @Body kelasModel mahasis);
}
