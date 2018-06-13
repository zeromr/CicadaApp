# 知了App
[![Build Status](https://travis-ci.org/xcc3641/SeeWeather.svg?branch=master)](https://travis-ci.org/xcc3641/SeeWeather)

知了App该应用就是如同它的名字一样，想做一个功能性相关的集合软件。

----

### 简介
知了App——是一款遵循 **Material Design** 风格的多功能APP。
- 半透明背景（当前天气情况，未来几天的天气情况，空气质量，生活建议）
- 缓存数据，减少网络请求，保证离线查看
- 内置天气图标


----

权限说明

```xml
	<!--用于进行网络定位-->
	<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
	<!--用于访问GPS定位-->
	<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
	<!--获取运营商信息，用于支持提供运营商信息相关的接口-->
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	<!--用于访问wifi网络信息，wifi信息会用于进行网络定位-->
	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	<!--这个权限用于获取wifi的获取权限，wifi信息会用来进行网络定位-->
	<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
	<!--用于访问网络，网络定位需要上网-->
	<uses-permission android:name="android.permission.INTERNET"/>
	<!--用于读取手机当前的状态-->
	<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	<!--写入扩展存储，向扩展卡写入数据，用于写入缓存定位数据-->
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

```

### TODO

- [x] 桌面小部件
- [x] 通知栏提醒
- [x] 城市选择
- [x] 自动定位
- [ ] 引导页面


----

### 项目
#### 公开 API

天气数据来源于：和风天气

城市信息来源于：郭霖大神提供的接口

地理定位服务： 高德地图

#### 开源技术
1. [Rxjava][2]
2. [RxAndroid][3]
3. [LitePal][4]
5. [GLide][5]
6. [okhttp][6]
7. [gson][7]
8. [RxPermissions][8]
9. [SwipeDelMenuLayout][9]
10. [butterknife][10]


### 截图

![][image-2]
![][image-3]
![][image-4]
![][image-5]

### 感谢
感谢开源，学习到了前辈们优秀的代码：
- [@张鸿洋][7]
- [@郭霖][8]
- [@泡在网上编代码][9]

### 关于作者

零

- [个人博客](http://blog.kingzero.cn)


[7]: https://github.com/hongyangAndroid

[image-2]: /images/startup.png
[image-3]: /images/weather.png
[image-4]: /images/leftslide.png
[image-5]: /images/area.png
