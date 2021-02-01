package com.bhavaniprasad.mycitiesweather.interfaces;
import com.bhavaniprasad.mycitiesweather.models.City;
import com.bhavaniprasad.mycitiesweather.models.Forecast;
//import com.bhavaniprasad.mycitiesweather.models.WeatherUpdate;

import java.util.List;


import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface Api {
    @GET("search")
    Call<List<City>> getWoeid(@Query("query") String city);
    @GET("{citycode}/{currentDate}")
    Call<List<Forecast>> getForecast(@Path("citycode") String citycode, @Path("currentDate") String currentDate);

}




