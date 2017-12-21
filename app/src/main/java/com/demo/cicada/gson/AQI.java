package com.demo.cicada.gson;

/**
 * Created by Zero on 2017/11/30.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
