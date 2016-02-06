package com.alorma.github.gcm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HerokuService {

  @POST("/register")
  Call<Object> register(@Body HerokuToken token);

}
