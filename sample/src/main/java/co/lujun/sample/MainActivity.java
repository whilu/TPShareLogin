package co.lujun.sample;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tencent.connect.share.QQShare;

import co.lujun.tpsharelogin.TPManager;
import co.lujun.tpsharelogin.bean.Config;
import co.lujun.tpsharelogin.bean.QQShareContent;
import co.lujun.tpsharelogin.bean.WBShareContent;
import co.lujun.tpsharelogin.bean.WXShareContent;
import co.lujun.tpsharelogin.listener.StateListener;
import co.lujun.tpsharelogin.platform.qq.QQManager;
import co.lujun.tpsharelogin.platform.weibo.WBManager;
import co.lujun.tpsharelogin.platform.weixin.WXManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnQQLogin, btnQQShare, btnQZoneShre, btnQQShareLocalImg,
            btnWXLogin, btnWXShare, btnWXShareTimeLine,
            btnWBLogin, btnWBShare, btnWBShareFromUrl;
    private QQManager qqManager;
    private WXManager wxManager;
    private WBManager wbManager;

    private final static String TAG = "TPShareLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQQLogin = (Button) findViewById(R.id.btn_qq_login);
        btnQQShare = (Button) findViewById(R.id.btn_qq_share);
        btnQZoneShre = (Button) findViewById(R.id.btn_qq_share_qzone);
        btnQQShareLocalImg = (Button) findViewById(R.id.btn_qq_share_local_image);
        btnWXLogin = (Button) findViewById(R.id.btn_wx_login);
        btnWXShare = (Button) findViewById(R.id.btn_wx_share);
        btnWXShareTimeLine = (Button) findViewById(R.id.btn_wx_share_timeline);
        btnWBLogin = (Button) findViewById(R.id.btn_wb_login);
        btnWBShare = (Button) findViewById(R.id.btn_wb_share);
        btnWBShareFromUrl = (Button) findViewById(R.id.btn_wb_share_url);

        btnQQLogin.setOnClickListener(this);
        btnQQShare.setOnClickListener(this);
        btnQZoneShre.setOnClickListener(this);
        btnQQShareLocalImg.setOnClickListener(this);
        btnWXLogin.setOnClickListener(this);
        btnWBShare.setOnClickListener(this);
        btnWXShare.setOnClickListener(this);
        btnWXShareTimeLine.setOnClickListener(this);
        btnWBLogin.setOnClickListener(this);
        btnWBShareFromUrl.setOnClickListener(this);

        //参数分别为微博回调地址、微博APP KEY、微博APP SECRET、QQ APPID、QQ APPSECRET、微信APPID、微信APPSECRET
        TPManager.getInstance().initAppConfig(
                "", "", "",
                "", "",
                "", "");
        qqManager = new QQManager(this);
        wxManager = new WXManager(this);
        wbManager = new WBManager(this);
        //
        StateListener<String> qqStateListener = new StateListener<String>() {

            @Override
            public void onComplete(String s) {
                Log.d(TAG, s);
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

        StateListener<String> wxStateListener = new StateListener<String>() {
            @Override
            public void onComplete(String s) {
                Log.d(TAG, s);
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
        wxManager.setListener(wxStateListener);

        StateListener<String> wbStateListener = new StateListener<String>() {
            @Override
            public void onComplete(String s) {
                Log.d(TAG, s);
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
        wbManager.setListener(wbStateListener);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_qq_login:// QQ登录
                qqManager.onLoginWithQQ();
                break;
            case R.id.btn_qq_share:// QQ分享图文消息
                QQShareContent contentQQ = new QQShareContent();
                contentQQ.setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                        .setTitle("TPShareLogin Test")
                        .setTarget_url("http://lujun.co")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setSummary("This is TPShareLogin test, 4 qq!");
                qqManager.share(contentQQ);
                break;
            case R.id.btn_qq_share_qzone:// QZONE分享图文消息
                QQShareContent contentQZone = new QQShareContent();
                contentQZone.setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                        .setShareExt(QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN)
                        .setTitle("TPShareLogin Test")
                        .setTarget_url("http://lujun.co")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setSummary("This is TPShareLogin test, 4 qq!");
                qqManager.share(contentQZone);
                break;
            case R.id.btn_qq_share_local_image:// QQ分享本地图片
                QQShareContent contentQQImage = new QQShareContent();
                contentQQImage.setShareType(QQShare.SHARE_TO_QQ_TYPE_IMAGE)
                        .setImage_path(Environment.getExternalStorageDirectory() + "/1234321.png");
                qqManager.share(contentQQImage);
                break;
            case R.id.btn_wx_login:// 微信登录
                wxManager.onLoginWithWX();
                break;
            case R.id.btn_wx_share:// 微信分享
                WXShareContent contentWX = new WXShareContent();
                //分享Text类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setType(WXShareContent.share_type.Text)
                        .setText("This is TPShareLogin test, 4 weixin!");*/
                //分享Webpage类型，微信分享需要缩略图的其他类型图片都会被压缩至32k以内
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setType(WXShareContent.share_type.WebPage)
                        .setWeb_url("http://lujun.co")
                        .setTitle("WebTitle")
                        .setDescription("Web description, description, description")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg");*/
                //分享Image类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setType(WXShareContent.share_type.Image)
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg");*/
                //分享Video类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setType(WXShareContent.share_type.Video)
                        .setVideo_url("http://lujun.co")
                        .setTitle("VideoTitle")
                        .setDescription("Video description, description, description")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg");*/
                //分享Music类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setType(WXShareContent.share_type.Music)
                        .setMusic_url("http://lujun.co")
                        .setTitle("MusicTitle")
                        .setDescription("Music description, description, description")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg");*/
                //分享Appdata类型
                contentWX.setScene(WXShareContent.WXSession)
                        .setType(WXShareContent.share_type.Appdata)
                        .setTitle("AppdataTitle")
                        .setDescription("Appdata description, description, description")
//                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setApp_data_path(Environment.getExternalStorageDirectory() + "/1234321.png");
                wxManager.share(contentWX);
                break;
            case R.id.btn_wx_share_timeline:// 微信朋友圈分享，分享类型同上分享到会话列表
                WXShareContent contentWX2 = new WXShareContent();
                contentWX2.setScene(WXShareContent.WXTimeline)
                        .setType(WXShareContent.share_type.Text)
                        .setText("This is TPShareLogin test, 4 weixin timeline!");
                wxManager.share(contentWX2);
                break;
            case R.id.btn_wb_login:// 微博登录
                wbManager.onLoginWithWB();
                break;
            case R.id.btn_wb_share_url:// 微博分享远程图片和文字消息
                WBShareContent contentWB = new WBShareContent();
                /////////////WBShareContent.API_SHARE(不推荐使用该方式分享，不会调起客户端，分享会在本应用内进行)
                //UPLOAD，普通发布微博API接口
                /*contentWB.setShare_method(WBShareContent.API_SHARE)
                        .setWbShareApiType(WBShareContent.UPLOAD)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
                        .setPic(Environment.getExternalStorageDirectory() + "/1234321.png");*/
                //UPLOAD_URL_TEXT，需要申请微博高级写入权限权限
                /*contentWB.setShare_method(WBShareContent.API_SHARE)
                        .setWbShareApiType(WBShareContent.UPLOAD_URL_TEXT)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
                        .setUrl("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg");*/
                /////////////WBShareContent.COMMON_SHARE(推荐使用该方式分享，将会调起客户端进行分享操作)
                //webpage及远程缩略图
                /*contentWB.setShare_method(WBShareContent.COMMON_SHARE)
                        .setContent_type(WBShareContent.WEBPAGE)
                        //setShare_type(int type)指定分享的策略，一般不需要指定
                        // 1、Config.SHARE_CLIENT(根据微博SDK版本而定分享策略，默认)
                        // 2、Config.SHARE_ALL_IN_ONE(将会指定所有的类型到一条内容中展示)
                        .setShare_type(Config.SHARE_CLIENT)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu")
                        // 在分享类型是WEBPAGE、MUSIC、VIDEO、VOICE的时候，
                        // setImage_url("url")和setImage_path("path")设置的是这四种基本类型的缩略图
                        // 如果需要在分享图文微博时带上链接，只需要在setStatus("content, http://...")中写上链接即可，微博会自动识别
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setTitle("title")
                        .setDescription("description")
                        .setActionUrl("http://lujun.co")
                        .setDataUrl("http://lujun.co")
                        .setDadtaHdUrl("http://lujun.co")
                        .setDefaultText("default action");*/
                //远程图片及文字，不通过setContent_type()方法指定类型，程序会自动识别
                contentWB.setShare_method(WBShareContent.COMMON_SHARE)
                        .setShare_type(Config.SHARE_CLIENT)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu  http://lujun.co")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg");
                //纯文字微博，，不通过setContent_type()方法指定类型，程序会自动识别
                /*contentWB.setShare_method(WBShareContent.COMMON_SHARE)
                        .setShare_type(Config.SHARE_CLIENT)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ");*/
                wbManager.share(contentWB);
                break;

            case R.id.btn_wb_share:// 微博分享本地图片和文字消息
                WBShareContent contentWB2 = new WBShareContent();
                /*contentWB2.setShare_method(WBShareContent.API_SHARE)
                        .setWbShareApiType(WBShareContent.UPLOAD)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
                        .setImage_path(Environment.getExternalStorageDirectory() + "/1234321.png");*/
                //webpage， 同上介绍的webpage分享，微博分享需要缩略图的其他类型图片都会被压缩至32k以内
                contentWB2.setShare_method(WBShareContent.COMMON_SHARE)
                        .setContent_type(WBShareContent.WEBPAGE)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
                        .setImage_path(Environment.getExternalStorageDirectory() + "/1234321.png")
                        .setTitle("title")
                        .setDescription("description")
                        .setActionUrl("http://lujun.co")
                        .setDataUrl("http://lujun.co")
                        .setDadtaHdUrl("http://lujun.co")
                        .setDefaultText("default action");
                // music
                /*contentWB2.setShare_method(WBShareContent.COMMON_SHARE)
                        .setContent_type(WBShareContent.MUSIC)
                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
                        .setImage_path(Environment.getExternalStorageDirectory() + "/1234321.png")
                        .setTitle("title")
                        .setDescription("description")
                        .setActionUrl("http://lujun.co")
                        .setDuration(10)
                        .setDefaultText("default action");*/
                // video voice...
                wbManager.share(contentWB2);
                break;
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
