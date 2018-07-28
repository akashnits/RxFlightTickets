package com.example.akash.rxflightticket.networkUtils;

import com.bumptech.glide.RequestBuilder;
import com.example.akash.rxflightticket.Constant;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static String TAG = ApiClient.class.getSimpleName();

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static int REQUEST_TIMEOUT = 60;

    public static Retrofit getRetrofit(){

        if(okHttpClient == null)
            initOkhttp();

        Retrofit.Builder retrofitBuilder= new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofitBuilder.build();
    }

    private static void initOkhttp(){
        OkHttpClient.Builder builder= new OkHttpClient.Builder()
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor= new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(httpLoggingInterceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest= chain.request();
                Request.Builder requestBuilder= originalRequest.newBuilder();
                requestBuilder.addHeader("Accept", "application/json")
                        .addHeader("Request-Type", "Android")
                        .addHeader("Content-Type", "application/json");
                return chain.proceed(requestBuilder.build());
            }
        });
        okHttpClient= builder.build();
    }
}
