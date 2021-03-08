package com.example.covid_check_program;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @POST("api/posts")
    Call<Userdata> createPost(@Body Userdata userdata);
}
