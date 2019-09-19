package com.topanlabs.unsoedpass;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface tokenint {

   @POST("/api-token-auth2/")
    Call<tokenmodel> getToken(@Body tokenmodel tokenmodel);
}