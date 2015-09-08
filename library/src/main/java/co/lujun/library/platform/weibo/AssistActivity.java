package co.lujun.library.platform.weibo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;

import co.lujun.library.bean.Config;
import co.lujun.library.bean.WBShareContent;
import co.lujun.library.listener.StateListener;
import co.lujun.library.platform.weibo.listener.AsyncRequestListener;
import co.lujun.library.platform.weibo.listener.AuthListener;
import co.lujun.library.platform.weibo.openapi.UsersAPI;

/**
 * Created by lujun on 2015/9/7.
 */
public class AssistActivity extends Activity implements IWeiboHandler.Response {


    private final static String SCOPE = "email,direct_messages_read,direct_messages_write,"
                                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                                        + "follow_app_official_microblog,invitation_write";

    private final static String TAG = "AssistActivity";

    private String appid;

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private UsersAPI mUsersAPI;
    private IWeiboShareAPI wbShareAPI;

    private Intent mIntent;
    private Bundle mBundle;

    private int apiType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        String redirectUrl = "";
        final int type = getIntent().getIntExtra(Config.KEY_OF_TYPE, 0x1000);
        apiType = getIntent().getIntExtra(Config.KEY_OF_API_TYPE, WBShareContent.UPLOAD);
        mBundle = getIntent().getBundleExtra(Config.KEY_OF_BUNDLE);
        if ((appid = getIntent().getStringExtra(Config.KEY_OF_APPID)) == null
                || (redirectUrl = getIntent().getStringExtra(Config.KEY_OF_REDIRECT_URL)) == null
                || type == 0x1000){
            finish();
        }

        mAuthInfo = new AuthInfo(this, appid, redirectUrl, SCOPE);
        mIntent = new Intent(Config.KEY_OF_WB_BCR_ACTION);

        mSsoHandler = new SsoHandler(this, mAuthInfo);
        AuthListener listener = new AuthListener();
        listener.setStateListener(new StateListener<Bundle>() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(AssistActivity.this, accessToken);
                if (type == Config.LOGIN_TYPE){
                    getUserInfo(accessToken);
                }else {
                    share(accessToken);
                }
            }

            @Override
            public void onError(String err) {
                Log.i(TAG, err);
                mIntent.putExtra(Config.KEY_OF_WB_BCR, err);
                onSendBroadCast();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel()");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "onCancel()");
                onSendBroadCast();
            }
        });
        mSsoHandler.authorize(listener);
    }

    /**
     * 微博获取用户信息
     * @param accessToken
     */
    private void getUserInfo(Oauth2AccessToken accessToken){
        if (accessToken != null && accessToken.isSessionValid()){
            mUsersAPI = new UsersAPI(this, appid, accessToken);
            AsyncRequestListener listener = new AsyncRequestListener();
            listener.setStateListener(new StateListener<String>() {
                @Override
                public void onComplete(String s) {
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, s);
                    onSendBroadCast();
                }

                @Override
                public void onError(String err) {
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, err);
                    onSendBroadCast();
                }

                @Override
                public void onCancel() {
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, "onCancel()");
                    onSendBroadCast();
                }
            });
            mUsersAPI.show(Long.parseLong(accessToken.getUid()), listener);
        }else {
            mIntent.putExtra(Config.KEY_OF_WB_BCR, "accessToken null or invalid!");
            onSendBroadCast();
        }
    }

    private void share(Oauth2AccessToken accessToken){
        if (accessToken != null && accessToken.isSessionValid() && mBundle != null) {
            WeiboParameters params = new WeiboParameters(appid);
            // 详见http://open.weibo.com/wiki/2/statuses/upload_url_text
//        params.put("source", appid); // 采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
            params.put("access_token", accessToken.getToken());// 采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得
            params.put("status", getBundleString("status"));
            params.put("visible",mBundle.getInt("visible", 0));
            params.put("list_id", getBundleString("list_id"));
            if (apiType == WBShareContent.UPLOAD){
                params.put("pic", BitmapFactory.decodeFile(getBundleString("pic")));
            }else {
                params.put("url", getBundleString("url"));
            }
//        params.put("pic_id", mBundle.getString("pic_id", ""));
//        params.put("lat", mBundle.getFloat("lat", 0.0f));
//        params.put("long", mBundle.getFloat("longt", 0.0f));
//        params.put("annotations",  mBundle.getString("annotations", ""));
//        params.put("rip", mBundle.getString("rip", ""));

            AsyncRequestListener listener = new AsyncRequestListener();
            listener.setStateListener(new StateListener() {
                @Override
                public void onComplete(Object o) {
                    Log.i(TAG, o.toString());
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, o.toString());
                    onSendBroadCast();
                }

                @Override
                public void onError(String err) {
                    Log.i(TAG, err);
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, err);
                    onSendBroadCast();
                }

                @Override
                public void onCancel() {
                    Log.i(TAG, "onCancel()");
                    mIntent.putExtra(Config.KEY_OF_WB_BCR, "onCancel()");
                    onSendBroadCast();
                }
            });
            String api = "https://api.weibo.com/2/statuses";
            api += apiType == WBShareContent.UPLOAD ? "/upload.json" : "/upload_url_text.json";
            AsyncWeiboRunner runner = new AsyncWeiboRunner(this);
            runner.requestAsync(
                    api,
                    params,
                    "POST",
                    listener);
        }else {
            mIntent.putExtra(Config.KEY_OF_WB_BCR, "accessToken null or invalid!");
            onSendBroadCast();
        }
    }

    private String getBundleString(String key){
        if (TextUtils.isEmpty(mBundle.getString(key))){
            return "";
        }
        return mBundle.getString(key);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode){
            case WBConstants.ErrorCode.ERR_OK:
                Log.i(TAG, "share successfull!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "share successfull!");
                onSendBroadCast();
                break;

            case WBConstants.ErrorCode.ERR_CANCEL:
                Log.i(TAG, "share canceled!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "share canceled!");
                onSendBroadCast();
                break;

            case WBConstants.ErrorCode.ERR_FAIL:
                Log.i(TAG, "share failed!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "share failed!");
                onSendBroadCast();
                break;

            default:
                Log.i(TAG, "unkown error!");
                mIntent.putExtra(Config.KEY_OF_WB_BCR, "unkown error!");
                onSendBroadCast();
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wbShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null){
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void onSendBroadCast(){
        sendBroadcast(mIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
