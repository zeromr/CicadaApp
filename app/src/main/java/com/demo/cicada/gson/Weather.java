package com.demo.cicada.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 天气预报类
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
