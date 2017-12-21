package com.demo.cicada.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zero on 2017/11/30.
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
