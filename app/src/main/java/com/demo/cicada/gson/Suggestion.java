package com.demo.cicada.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 生活指数
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;             //  舒适度指数
    @SerializedName("cw")
    public CarWash carWash;             // 洗车指数
    public Sport sport;                 // 运动指数
    @SerializedName("drsg")
    public Dress dress;                 // 穿衣指数
    @SerializedName("flu")
    public Cold cold;                   // 感冒指数
    @SerializedName("trav")
    public Travel travel;                // 旅游指数
    public UV uv;                       // 紫外线指数

    public class Comfort {
        @SerializedName("brf")      // 生活指数简介
        public String brief;
        @SerializedName("txt")
        public String info;         // 生活指数详细描述
    }

    public class CarWash {
        @SerializedName("brf")      // 生活指数简介
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("brf")      // 生活指数简介
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Dress {
        @SerializedName("brf")      // 生活指数简介
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Cold {
        @SerializedName("brf")      // 生活指数简介
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Travel {
        @SerializedName("brf")      // 生活指数简介
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class UV {
        @SerializedName("brf")      // 生活指数简介
        public String brief;
        @SerializedName("txt")
        public String info;
    }
}
