package com.example.task.Data;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static String  BASE_URL = "http://demo1585915.mockable.io/";


    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS);
            httpClient.readTimeout(240, TimeUnit.SECONDS);
            httpClient.connectTimeout(240, TimeUnit.SECONDS);
            httpClient.writeTimeout(240, TimeUnit.SECONDS);
// add your other interceptors …

// add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the imp

            retrofit = new Retrofit.Builder()

                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }


}
