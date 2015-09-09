package co.lujun.tpsharelogin.bean;

import android.os.Bundle;

import com.tencent.connect.share.QQShare;

/**
 * Created by lujun on 2015/9/6.
 */
public class QQShareContent {

    //这条分享消息被好友点击后的跳转URL
    private String target_url;

    //分享的标题
    private String title;

    //分享的图片URL
    private String image_url;

    //分享的消息摘要，最长50个字
    private String summary;

    //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
    private String appname;

    //标识该消息的来源应用，值为应用名称+AppId
    private String appsource;

    //bundle
    private Bundle mBundle;

    public QQShareContent(){
        mBundle = new Bundle();
    }
    
    public QQShareContent setImage_url(String image_url) {
        this.image_url = image_url;
        mBundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, image_url);
        return this;
    }

    public QQShareContent setTarget_url(String target_url) {
        this.target_url = target_url;
        mBundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, target_url);
        return this;
    }

    public QQShareContent setTitle(String title) {
        this.title = title;
        mBundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        return this;
    }

    public QQShareContent setSummary(String summary) {
        this.summary = summary;
        mBundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        return this;
    }

    public QQShareContent setAppname(String appname) {
        this.appname = appname;
        mBundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appname);
        return this;
    }

    public QQShareContent setAppsource(String appsource) {
        this.appsource = appsource;
        mBundle.putString(QQShare.SHARE_TO_QQ_KEY_TYPE, appsource);
        return this;
    }

    public String getTarget_url() {
        return target_url;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getSummary() {
        return summary;
    }

    public String getAppname() {
        return appname;
    }

    public String getAppsource() {
        return appsource;
    }

    public Bundle getBundle() {
        return mBundle;
    }

}
