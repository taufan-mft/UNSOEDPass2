package com.topanlabs.unsoedpass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface kelasInt {

    @GET("/retkelas/{nim}/")
    Call<List<kelasModel>> getKelas(@Header("Authorization") String token, @Path("nim") String nim);
}
