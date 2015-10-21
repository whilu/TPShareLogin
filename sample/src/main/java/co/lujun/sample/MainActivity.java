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
                "http://lujun.co", "1801471434", "6c4bf7a0f7f757b6ae3288ab2a53046e",
                "1104730321", "9QoiADlAfE5TlMZQ",
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
                contentQQ
                        .setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                        .setTitle("TPShareLogin Test")
                        .setTarget_url("http://lujun.co")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setSummary("This is TPShareLogin test, 4 qq!");
                qqManager.share(contentQQ);
                break;
            case R.id.btn_qq_share_qzone:// QZONE分享图文消息
                QQShareContent contentQZone = new QQShareContent();
                contentQZone
                        .setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
                        .setTitle("TPShareLogin Test")
                        .setTarget_url("http://lujun.co")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setSummary("This is TPShareLogin test, 4 qq!")
                        .setShareExt(QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                qqManager.share(contentQZone);
                break;
            case R.id.btn_qq_share_local_image:// QQ分享本地图片
                QQShareContent contentQQImage = new QQShareContent();
                contentQQImage
                        .setShareType(QQShare.SHARE_TO_QQ_TYPE_IMAGE)
                        .setImage_path(Environment.getExternalStorageDirectory() + "/1234321.png");
                qqManager.share(contentQQImage);
                break;
            case R.id.btn_wx_login:// 微信登录
                wxManager.onLoginWithWX();
                break;
            case R.id.btn_wx_share:// 微信分享
                WXShareContent contentWX = new WXShareContent();

                //分享Text类型
                /*contentWX.setText("This is TPShareLogin test, 4 weixin!")
                        .setScene(WXShareContent.WXSession)
                        .setType(WXShareContent.share_type.Text);*/
                //分享Webpage类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setWeb_url("http://lujun.co")
                        .setTitle("WebTitle")
                        .setDescription("Web description, description, description")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setType(WXShareContent.share_type.WebPage);*/
                //分享Image类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setType(WXShareContent.share_type.Image);*/
                //分享Video类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setVideo_url("http://lujun.co")
                        .setTitle("VideoTitle")
                        .setDescription("Video description, description, description")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setType(WXShareContent.share_type.Video);*/
                //分享Music类型
                /*contentWX.setScene(WXShareContent.WXSession)
                        .setMusic_url("http://lujun.co")
                        .setTitle("MusicTitle")
                        .setDescription("Music description, description, description")
                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setType(WXShareContent.share_type.Music);*/
                //分享Appdata类型
                contentWX.setScene(WXShareContent.WXSession)
                        .setTitle("AppdataTitle")
                        .setDescription("Appdata description, description, description")
//                        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setApp_data_path(Environment.getExternalStorageDirectory() + "/1234321.png")
                        .setType(WXShareContent.share_type.Appdata);
                wxManager.share(contentWX);
                break;
            case R.id.btn_wx_share_timeline:// 微信朋友圈分享
                WXShareContent contentWX2 = new WXShareContent();
                contentWX2.setText("This is TPShareLogin test, 4 weixin timeline!")
                        .setScene(WXShareContent.WXTimeline)
                        .setType(WXShareContent.share_type.Text);
                wxManager.share(contentWX2);
                break;
            case R.id.btn_wb_login:// 微博登录
                wbManager.onLoginWithWB();
                break;
            case R.id.btn_wb_share_url:// 微博分享远程图片和文字消息
                WBShareContent contentWB = new WBShareContent();
                //UPLOAD，普通发布微博API接口
                /*contentWB.setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
                        .setPic(Environment.getExternalStorageDirectory() + "/1234321.png")
                        .setWbShareApiType(WBShareContent.UPLOAD);*/
                //UPLOAD_URL_TEXT，需要申请微博权限
                contentWB.setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
                        .setUrl("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
                        .setWbShareApiType(WBShareContent.UPLOAD_URL_TEXT);
                wbManager.share(contentWB);
                break;

            case R.id.btn_wb_share:// 微博分享本地图片和文字消息
                WBShareContent contentWB2 = new WBShareContent();
//                contentWB2.setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
//                        .setImage_path(Environment.getExternalStorageDirectory() + "/1234321.png")
//                        .setWbShareApiType(WBShareContent.UPLOAD);
                //webpage
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
//                contentWB2.setShare_method(WBShareContent.COMMON_SHARE)
//                        .setContent_type(WBShareContent.MUSIC)
//                        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
//                        .setImage_path(Environment.getExternalStorageDirectory() + "/1234321.png")
//                        .setTitle("title")
//                        .setDescription("description")
//                        .setActionUrl("http://lujun.co")
//                        .setDuration(10)
//                        .setDefaultText("default action");
                // video
                // voice
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
