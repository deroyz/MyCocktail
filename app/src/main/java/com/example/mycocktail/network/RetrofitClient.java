package com.example.mycocktail.network;

import com.example.mycocktail.MainActivity;
import com.example.mycocktail.network.RetrofitInterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient retrofitClient = null;
    private static RetrofitInterface retrofitInterface;
    private static final String API_KEY = "9973533";
    private static final String BASE_URL = "http://www.thecocktaildb.com/";

    private RetrofitClient() {


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }


    public static RetrofitClient getRetrofitClient() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;

    }

    public static RetrofitInterface getRetrofitInterface() {
        return retrofitInterface;
    }
}
