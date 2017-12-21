package com.demo.cicada.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zero on 2017/11/30.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
