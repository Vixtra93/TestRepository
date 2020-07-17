package com.example.myapplication.data;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String MORTY_ITEMS_URL = "https://rickandmortyapi.com/";
    private static ApiClient apiClient;
    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder adapterBuilder;
    private static final long TIME_OUT = 10000;

    private ApiClient() {
        createDefaultAdapter();
    }


    public <T> T createService(Context context, Class<T> serviceClass) {
        return adapterBuilder.client(okBuilder.build())
                .baseUrl(MORTY_ITEMS_URL)
                .build()
                .create(serviceClass);
    }

    public void createDefaultAdapter() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okBuilder = new OkHttpClient.Builder();
        okBuilder.addInterceptor(logging);

        adapterBuilder = new Retrofit.Builder().client(
                okBuilder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                        .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create());
    }


    public static synchronized ApiClient  getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }

        return apiClient;
    }

}
