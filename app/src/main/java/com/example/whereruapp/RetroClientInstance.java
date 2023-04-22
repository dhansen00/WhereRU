package com.example.whereruapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A class that makes connecting to the REST API easier
 */
public class RetroClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://whererunodejs.herokuapp.com/";

    /**
     * @return returns a Retrofit to make connecting to the REST API easier
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            ScalarsConverterFactory factory = ScalarsConverterFactory.create();
            GsonConverterFactory factory1 = GsonConverterFactory.create();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(factory)
                    .addConverterFactory(factory1)
                    .build();
        }
        return RetroClientInstance.retrofit;
    }
}
