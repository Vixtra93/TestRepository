package com.example.myapplication.data;

import com.example.myapplication.domain.Response;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ItemService {


    @GET("api/character/")
    Call<Response> getItems(@Query("page") int page, @QueryMap(encoded = true) Map<String, String> query);


}
