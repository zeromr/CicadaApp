package com.demo.cicada.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 实况天气
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
