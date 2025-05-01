package com.example.myapplication.Askme;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GeminiApiService {
    @POST("AIzaSyCQ8-JuVK9wu8e1TV_VYfJdbVltF03kK2Y") // Replace with the actual endpoint
    Call<GeminiResponse> sendMessage(@Body GeminiRequest request);
}

