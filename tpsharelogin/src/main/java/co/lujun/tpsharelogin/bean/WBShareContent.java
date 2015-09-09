package co.lujun.tpsharelogin.bean;

import android.os.Bundle;

/**
 * Created by lujun on 2015/9/7.
 */
public class WBShareContent {

    // 提供两种API接口
    // Upload上传图片并发布一条微博
    // UPLOAD_URL_TEXT发布一条微博同时指定上传的图片或图片url
    public final static int UPLOAD = 1;
    public final static int UPLOAD_URL_TEXT = 2;

    public WBShareContent(){
        mBundle = new Bundle();
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public int getWbShareApiType() {
        return wbShareApiType;
    }

    public WBShareContent setStatus(String status) {
        this.status = status;
        mBundle.putString("status", status);
        return this;
    }

    public WBShareContent setVisible(int visible) {
        this.visible = visible;
        mBundle.putInt("visible", visible);
        return this;
    }

    public WBShareContent setList_id(String list_id) {
        this.list_id = list_id;
        mBundle.putString("list_id", list_id);
        return this;
    }

    public WBShareContent setPic(String pic) {
        this.pic = pic;
        mBundle.putString("pic", pic);
        return this;
    }

    public WBShareContent setUrl(String url) {
        this.url = url;
        mBundle.putString("url", url);
        return this;
    }

    public WBShareContent setPic_id(String pic_id) {
        this.pic_id = pic_id;
        mBundle.putString("pic_id", pic_id);
        return this;
    }

    public WBShareContent setLat(float lat) {
        this.lat = lat;
        mBundle.putFloat("lat", lat);
        return this;
    }

    public WBShareContent setLongt(float longt) {
        this.longt = longt;
        mBundle.putFloat("longt", longt);
        return this;
    }

    public WBShareContent setAnnotations(String annotations) {
        this.annotations = annotations;
        mBundle.putString("annotations", annotations);
        return this;
    }

    public WBShareContent setRip(String rip) {
        this.rip = rip;
        mBundle.putString("rip", rip);
        return this;
    }

    public WBShareContent setWbShareApiType(int wbShareApiType) {
        this.wbShareApiType = wbShareApiType;
        return this;
    }

    private String status;// 要发布的微博文本内容，必须做URLencode，内容不超过140个汉字

    private int visible;// 微博的可见性，0：所有人能看，1：仅自己可见，2：密友可见，3：指定分组可见，默认为0

    private String list_id;// 微博的保护投递指定分组ID，只有当visible参数为3时生效且必选
    
    private String pic;// 本地图片path

    private String url;// 图片的URL地址，必须以http开头

    private String pic_id;// 已经上传的图片pid，多个时使用英文半角逗号符分隔，最多不超过9个

    private float lat;// 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0

    private float longt;// 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0

    private String annotations;// 元数据

    private String rip;// 开发者上报的操作用户真实IP，形如：211.156.0.1

    private Bundle mBundle;

    private int wbShareApiType;
}
