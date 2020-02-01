package com.topanlabs.unsoedpass;

import com.topanlabs.unsoedpass.memo.memoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface kelasInt {

    @GET("/retkelas/{nim}/")
    Call<List<kelasModel>> getKelas(@Header("Authorization") String token, @Path("nim") String nim);

    @GET("/retmemo/{nim}/")
    Call<List<memoModel>> getMemo(@Header("Authorization") String token, @Path("nim") String nim);

    @GET("/liatkelas/{kodekelas}/")
    Call<kelasModel> cariKelas(@Path("kodekelas") String kodekelas);

    @POST("/createkelas/")
    Call<kelasModel> buatKelas(@Header("Authorization")String token, @Body kelasModel mahasis);

    @POST("/creatematkul/")
    Call<kelasModel> buatMatkul(@Header("Authorization")String token, @Body kelasModel mahasis);

    @DELETE("/deletekelas/{nim}/")
    Call<Void> delKelas(@Header("Authorization")String token,@Path("nim") String nim);
}
