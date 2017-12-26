package com.demo.cicada.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.demo.cicada.R;
import com.demo.cicada.gson.Forecast;
import com.demo.cicada.gson.Weather;
import com.demo.cicada.utils.HttpUtil;
import com.demo.cicada.utils.Utility;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity implements View.OnClickListener {
    private ScrollView svWeather;
    private TextView tvTitleCity;
    private TextView tvUpdateTime;
    private TextView tvDegree;
    private TextView tvWeatherInfo;
    private LinearLayout llForecast;
    private TextView tvAQI;
    private TextView tvPM25;
    private TextView tvComfort;
    private TextView tvCarWash;
    private TextView tvSport;
    private ImageView ivBingPic;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private String mWeatherId;
    private long clickTime = 0;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Log.i("msg", "城市信息: " + aMapLocation.getCity());
                    //获取定位时间+
                    /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date = new Date(aMapLocation.getTime());*/
//                    Log.i("msg", "时间: " + df.format(date));
                    //                    requestWeather(aMapLocation.getCity());
                    requestWeather(aMapLocation.getDistrict());
                    mLocationClient.stopLocation();     //停止定位后，本地定位服务并不会被销毁
                } else {
                    Toast.makeText(WeatherActivity.this, "定位失败，加载默认城市", Toast.LENGTH_SHORT).show();
                    requestWeather("德阳");
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" +
                            aMapLocation.getErrorInfo());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildVersion();
        setContentView(R.layout.activity_weather);

        initView();
        menuItem();
        loadData();
    }

    // 获取位置
    public void getLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();

        Log.i("msg", "getLocation: 定位已启动...");
    }

    /**
     * 检查是否已授予权限
     */
    private void checkPermission() {

        //need to check permission above android 6.0
        RxPermissions rxPermissions = new RxPermissions(this);

        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION).subscribe(granted -> {
            try {
                if (granted) {
                    Log.i("msg", "checkPermission: 已获得定位权限");
                    getLocation();
                } else {
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            Toast.makeText(getApplicationContext(), "未获得定位权限，加载默认城市", Toast.LENGTH_SHORT).show();
                        }
                    }*/
                    Toast.makeText(getApplicationContext(), "未获得定位权限，加载默认城市", Toast.LENGTH_SHORT).show();
                    requestWeather("德阳");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    // 加载天气和图片数据
    public void loadData() {
        //        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        loadImage();
        loadBgImage(ivBingPic,R.drawable.ic_bg_weather);
        svWeather.setVisibility(View.INVISIBLE);
        checkPermission();
        /*String weatherStr = sp.getString("weather", null);
        if (weatherStr != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherStr);
            if (weather != null) {
                mWeatherId = weather.basic.weatherId;
            }
            showWeatherInfo(weather);

        } else {
            svWeather.setVisibility(View.INVISIBLE);
            checkPermission();
        }*/
        swipeRefresh.setOnRefreshListener(() -> requestWeather(mWeatherId));
    }

    // 菜单项
    public void menuItem() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.nav_weather);
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_weather:
                    break;
                case R.id.nav_music:
                    Intent intentMusic = new Intent();
                    intentMusic.setClass(this, MusicActivity.class);
                    startActivity(intentMusic);
                    break;
                case R.id.nav_about:
                    startActivity(new Intent(this, AboutActivity.class));
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    // 初始化控件
    public void initView() {
        svWeather = (ScrollView) findViewById(R.id.sv_weather);
        tvTitleCity = (TextView) findViewById(R.id.tv_title_city);
        tvUpdateTime = (TextView) findViewById(R.id.tv_update_time);
        tvDegree = (TextView) findViewById(R.id.tv_degree);
        tvWeatherInfo = (TextView) findViewById(R.id.tv_weather_info);
        llForecast = (LinearLayout) findViewById(R.id.ll_forecast);
        tvAQI = (TextView) findViewById(R.id.tv_aqi);
        tvPM25 = (TextView) findViewById(R.id.tv_pm25);
        tvComfort = (TextView) findViewById(R.id.tv_comfort);
        tvCarWash = (TextView) findViewById(R.id.tv_car_wash);
        tvSport = (TextView) findViewById(R.id.tv_sport);
        ivBingPic = (ImageView) findViewById(R.id.iv_bing_pic);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Button btnNav = (Button) findViewById(R.id.btn_nav);
        Button btnMenu = (Button) findViewById(R.id.btn_menu);
        btnNav.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_nav:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = new Date().getTime();
            if ((currentTime - clickTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再次点击退出程序!", Toast.LENGTH_SHORT).show();
                clickTime = currentTime;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param weatherId
     */
    public void requestWeather(final String weatherId) {
        /*String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=bc0418b57b2d4918819d3974ac1285d9";*/
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=3ab404ccea9d416abe84dfe936af6f07";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败!", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences
                                    (WeatherActivity.this).edit();
                            edit.putString("weather", responseText);
                            edit.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败!", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
//        loadImage();
        loadBgImage(ivBingPic,R.drawable.ic_bg_weather);
    }

    /**
     * 处理并显示Weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        tvTitleCity.setText(cityName);
        tvUpdateTime.setText(updateTime);
        tvDegree.setText(degree);
        tvWeatherInfo.setText(weatherInfo);
        llForecast.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.weather_forecast_item, llForecast, false);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
            TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
            TextView tvMax = (TextView) view.findViewById(R.id.tv_max);
            TextView tvMin = (TextView) view.findViewById(R.id.tv_min);
            tvDate.setText(forecast.date);
            tvInfo.setText(forecast.more.info);
            tvMax.setText(forecast.temperature.max + "℃");
            tvMin.setText(forecast.temperature.min + "℃");
            llForecast.addView(view);
        }
        if (weather.aqi != null) {
            tvAQI.setText(weather.aqi.city.aqi);
            tvPM25.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        tvComfort.setText(comfort);
        tvCarWash.setText(carWash);
        tvSport.setText(sport);
        svWeather.setVisibility(View.VISIBLE);
    }

}
