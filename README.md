# TPShareLogin [![Build Status](https://travis-ci.org/whilu/TPShareLogin.svg)](https://travis-ci.org/whilu/TPShareLogin)
一个集成微博、微信和QQ第三方登录及分享功能的库

## 开始使用
### 引入库
#### Gradle
```xml
dependencies {
    compile 'co.lujun:tpsharelogin:1.0.0'
}
```

#### 本地导入library使用
######1、导入tpsharelogin库
######2、在Project下的settings.gradle中添加`include ':tpsharelogin'`
######3、使用该库的Module/build.gradle中dependencies语句添加`compile project(':tpsharelogin')`

### 使用
首先，在AndroidManifest.xml中配置需要的权限
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```
再在AndroidManifest.xml中配置第三方平台的信息
```xml
<!--QQ-->
<activity
    android:name="com.tencent.tauth.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <!-- 需要改写此处的appId，将tencent后面xxx的改为自己申请的appId，如tencent129068312-->
        <data android:scheme="tencentxxx" />
    </intent-filter>
</activity>

<!-- Weixin-->
<activity android:name=".wxapi.WXEntryActivity"
    android:exported="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.NoDisplay"/>

<!-- Weibo-->
```
其中`WXEntryActivity`位于`程序包名.wxapi`下，继承自`co.lujun.tpsharelogin.platform.weixin.AssistActivity`，如下：
```groovy
package co.lujun.sample.wxapi;
import co.lujun.tpsharelogin.platform.weixin.AssistActivity;

public class WXEntryActivity extends AssistActivity {
}
```

然后开始授权登录、分享！
#####a. 在程序自定义的Application类中实例化TPManager
```groovy
//参数分别为微博回调地址、微博APP KEY、微博APP SECRET、QQ APPID、QQ APPSECRET、微信APPID、微信APPSECRET
TPManager.getInstance().initAppConfig(
        "http://lujun.co", "", "",
        "", "",
        "", "");
```
#####b. 登录及分享
分别提供了`QQManager`、`WXManager`和`WBManager`用于QQ、微信及微博的登录与分享的实现
######QQ登录及分享
```groovy
QQManager qqManager = new QQManager(this);
StateListener<Object> qqStateListener = new StateListener<Object>() {
    @Override
    public void onComplete(Object o) {
        Log.d(TAG, o.toString());
    }

    @Override
    public void onError(String err) {
        Log.d(TAG, err);
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "onCancel()");
    }
};
qqManager.setListener(qqStateListener);
```
```groovy
//QQ登录
qqManager.onLoginWithQQ();
```
```groovy
//QQ分享
QQShareContent contentQQ = new QQShareContent();
contentQQ.setTitle("TPShareLogin Test")
        .setTarget_url("http://lujun.co")
        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
        .setSummary("This is TPShareLogin test, 4 qq!");
qqManager.share(contentQQ);
```
######微信登录及分享
```groovy
WXManager wxManager = new WXManager(this);
wxManager.setListener(StateListener<String>);
//微信登录
wxManager.onLoginWithWX();
//微信分享
WXShareContent contentWX = new WXShareContent();
wxManager.share(contentWX);
```
######微博登录及分享
```groovy
WBManager wbManager = new WBManager(this);
wbManager.setListener(StateListener<String>);
//微博登录
wbManager.onLoginWithWB();
//微博分享
WBShareContent contentWB = new WBShareContent();
wbManager.share(contentWB);
```
更多详细使用请见[Sample](https://github.com/whilu/TPShareLogin/tree/master/sample)示例。

## 注意事项
### 依赖库冲突
本库使用了[Retrofit](https://github.com/square/retrofit)、[RxAndroid](https://github.com/ReactiveX/RxAndroid)及[RxJava](https://github.com/ReactiveX/RxJava)等库，若你的项目中也使用了这些依赖库并发生了冲突，请在添加本库依赖时如下进行：
```xml
dependencies {
    compile ('co.lujun:tpsharelogin:1.0.0'){
        exclude module:'retrofit'
        exclude module:'rxjava'
        exclude module:'rxandroid'
    }
}
```
### 混淆
```groovy
-keep class com.tencent.mm.sdk.** {*;}
-keep class com.sina.**{*;}
-keep class * extends android.app.Dialog
```

## 关于
根据[微信开发文档](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&lang=zh_CN)、[微博开发文档](http://open.weibo.com/wiki/%E9%A6%96%E9%A1%B5)和[QQ开发文档](http://wiki.connect.qq.com/)开发，参考[ShareLoginLib](https://github.com/lingochamp/ShareLoginLib)，有任何问题，[Email me](mailto:lujunat1993@gmail.com).

## License
Copyright (c) 2015 [lujun](http://lujun.co)

Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
