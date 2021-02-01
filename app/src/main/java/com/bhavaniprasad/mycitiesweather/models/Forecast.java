package com.bhavaniprasad.mycitiesweather.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Forecast {
    //    {"id":5005832400928768,"weather_state_name":"Heavy Cloud","weather_state_abbr":"hc","wind_direction_compass":"ESE",
//    "created":"2021-01-31T10:11:36.442222Z","applicable_date":"2021-01-31","min_temp":18.59,"max_temp":28.990000000000002,
//    "the_temp":28.299999999999997,"wind_speed":5.5376395406665075,"wind_direction":111.17155476403877,"air_pressure":1015.5,
//    "humidity":49,"visibility":12.308120575837112,"predictability":71}


    @SerializedName("weather_state_abbr")
    @Expose
    private String weather_state_abbr;


    @SerializedName("weather_state_name")
    @Expose
    private String weather_state_name;

    @SerializedName("min_temp")
    @Expose
    private Double min_temp;

    @SerializedName("max_temp")
    @Expose
    private Double max_temp;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    @SerializedName("wind_speed")
    @Expose
    private Double wind_speed;

    @SerializedName("the_temp")
    @Expose
    private Double the_temp;

    @SerializedName("air_pressure")
    @Expose
    private Double air_pressure;


    public String getWeather_state_abbr() {
        return weather_state_abbr;
    }

    public void setWeather_state_abbr(String weather_state_abbr) {
        this.weather_state_abbr = weather_state_abbr;
    }

    public Double getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(Double min_temp) {
        this.min_temp = min_temp;
    }

    public Double getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(Double max_temp) {
        this.max_temp = max_temp;
    }

    public Double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(Double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public Double getAir_pressure() {
        return air_pressure;
    }

    public void setAir_pressure(Double air_pressure) {
        this.air_pressure = air_pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Double getThe_temp() {
        return the_temp;
    }

    public void setThe_temp(Double the_temp) {
        this.the_temp = the_temp;
    }

    public String getWeather_state_name() {
        return weather_state_name;
    }

    public void setWeather_state_name(String weather_state_name) {
        this.weather_state_name = weather_state_name;
    }



}
