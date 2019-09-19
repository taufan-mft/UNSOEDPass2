package com.topanlabs.unsoedpass;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface checkoutint {
    @POST("/order/")
    Call<checkoutmodel> createOrder(@Body checkoutmodel checkoutmodel, @Header("Authorization") String token);

    @GET("/orderstat/{nim}/")
    Call<checkoutmodel> getOrder(@Path("nim") String nim, @Header("Authorization") String token);
}
