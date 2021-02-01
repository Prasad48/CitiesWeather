package com.bhavaniprasad.mycitiesweather.repo;

import android.util.Log;

import com.bhavaniprasad.mycitiesweather.interfaces.Api;

import org.jetbrains.annotations.NotNull;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGenerator {

    private static final String TAG = "RetrofitGenerator";
    private static final String BASE_URL = "http://www.metaweather.com/api/location/";
    public static final String HEADER_PRAGMA = "Pragma";

    private static RetrofitGenerator instance;

    public static RetrofitGenerator getInstance(){
        if(instance == null){
            instance = new RetrofitGenerator();
        }
        return instance;
    }


    private static Retrofit retrofit(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor())
                .build();
    }


    private static HttpLoggingInterceptor httpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
                {
                    @Override
                    public void log (@NotNull String message)
                    {
                        Log.d(TAG, "log: http log: " + message);
                    }
                } );
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public static Api getApi(){
        return retrofit().create(Api.class);
    }

}

