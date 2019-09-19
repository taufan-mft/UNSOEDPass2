package com.topanlabs.unsoedpass;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface statint {

    @GET("/retstat/{nim}/")
    Call<statmodel> getStat(@Path("nim") String nim, @Header("Authorization") String token);

    @POST("/createstat/")
    Call<statmodel> createStat(@Body statmodel statmodel, @Header("Authorization") String token);

    @PUT("/retstat/{nim}/")
    Call<statmodel> updateStat(@Path("nim") String nim, @Body statmodel statmodel, @Header("Authorization") String token);
}
