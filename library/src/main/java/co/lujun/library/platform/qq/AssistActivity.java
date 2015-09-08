package co.lujun.library.platform.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import co.lujun.library.bean.Config;
import co.lujun.library.listener.StateListener;
import co.lujun.library.platform.qq.listener.BaseUiListener;

/**
 * Created by lujun on 2015/9/6.
 */
public class AssistActivity extends Activity {

    private Tencent mTencent;
    private BaseUiListener mListener,  mUserInfoListener;
    private Intent mIntent;

    private final static String TAG = "AssistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        String appid;
        final int type = getIntent().getIntExtra(Config.KEY_OF_TYPE, 0x1000);
        final Bundle bundle = getIntent().getBundleExtra(Config.KEY_OF_BUNDLE);
        if ((appid = getIntent().getStringExtra(Config.KEY_OF_APPID)) == null
                || type == 0x1000){
            finish();
        }

        mListener = new BaseUiListener();
        mListener.setListener(new StateListener<Object>() {
            @Override
            public void onComplete(Object o) {
                if (type == Config.LOGIN_TYPE) {
//                    getUserInfo(o);
                    //
                    if (o == null){
                        Log.e(TAG, "get auth info error!");
                        mIntent.putExtra(Config.KEY_OF_QQ_BCR, "get auth info error!");
                        onSendBroadCast();
                    }
                    JSONObject jsonObject = (JSONObject) o;
                    String accessToken = "";
                    String expires_in = "";
                    String openId = "";
                    try {
                        accessToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                        expires_in = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                        openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                        if (TextUtils.isEmpty(accessToken)
                                || TextUtils.isEmpty(expires_in)
                                || TextUtils.isEmpty(openId)){
                            Log.e(TAG, "get auth info null!");
                            mIntent.putExtra(Config.KEY_OF_QQ_BCR, "get auth info null!");
                            onSendBroadCast();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    mIntent.putExtra(Config.KEY_OF_TYPE, Config.LOGIN_TYPE);
                    mIntent.putExtra(Config.KEY_OF_QQ_BCR, "get user information!");
                    mIntent.putExtra(Config.KEY_OF_ACCESS_TOKEN, accessToken);
                    mIntent.putExtra(Config.KEY_OF_EXPIRES_IN, expires_in);
                    mIntent.putExtra(Config.KEY_OF_OPEN_ID, openId);
                    onSendBroadCast();
                }else{
                    mIntent.putExtra(Config.KEY_OF_QQ_BCR, "share successful!");
                    onSendBroadCast();
                }
            }

            @Override
            public void onError(String s) {
                Log.e(TAG, s);
                mIntent.putExtra(Config.KEY_OF_QQ_BCR, s);
                onSendBroadCast();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel()");
                mIntent.putExtra(Config.KEY_OF_QQ_BCR, "onCancel()");
                onSendBroadCast();
            }
        });

        mIntent = new Intent(Config.KEY_OF_QQ_BCR_ACTION);
        mTencent = Tencent.createInstance(appid, this);
        if (type == Config.LOGIN_TYPE){
            mTencent.login(this, "all", mListener);
        }else if (type == Config.SHARE_TYPE){
            if (bundle != null) {
                mTencent.shareToQQ(this, bundle, mListener);
            }else {
                mIntent.putExtra(Config.KEY_OF_QQ_BCR, "share content is null!");
                onSendBroadCast();
            }
        }else {
            mIntent.putExtra(Config.KEY_OF_QQ_BCR, "action type is null!");
            onSendBroadCast();
        }
    }

    /**
     * 获取用户信息，因为QQ获取用户信息不是在QQ客户端进行，所以改用回调至调用的程序中进行
     * @param o
     * @deprecated
     */
    private void getUserInfo(Object o){
        /*if (o == null){
            Log.e(TAG, "get auth info error!");
            mIntent.putExtra(Config.KEY_OF_QQ_BCR, "get auth info error!");
            onSendBroadCast();
        }
        JSONObject jsonObject = (JSONObject) o;
        try {
            String accessToken = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires_in = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (TextUtils.isEmpty(accessToken)
                    || TextUtils.isEmpty(expires_in)
                    || TextUtils.isEmpty(openId)){
                Log.e(TAG, "get auth info null!");
                mIntent.putExtra(Config.KEY_OF_QQ_BCR, "get auth info null!");
                onSendBroadCast();
            }
            mTencent.setAccessToken(accessToken, expires_in);
            mTencent.setOpenId(openId);
        }catch (JSONException e){
            e.printStackTrace();
        }

        //new method
//        mTencent.requestAsync(Constants.GRAPH_BASE, null, Constants.HTTP_GET, new BaseApiListener(), null);
        //old method
        UserInfo userInfo = new UserInfo(this, mTencent.getQQToken());
        mUserInfoListener = new BaseUiListener();
        mUserInfoListener.setListener(new StateListener<Object>() {
            @Override
            public void onComplete(Object o) {
//                Log.i(TAG, o.toString());
                mIntent.putExtra(Config.KEY_OF_QQ_BCR, o.toString());
                onSendBroadCast();
            }

            @Override
            public void onError(String s) {
                Log.e(TAG, s);
                mIntent.putExtra(Config.KEY_OF_QQ_BCR, s);
                onSendBroadCast();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel()");
                mIntent.putExtra(Config.KEY_OF_QQ_BCR, "onCancel()");
                onSendBroadCast();
            }
        });
        userInfo.getUserInfo(mUserInfoListener);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    private void onSendBroadCast(){
        sendBroadcast(mIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTencent){
            mTencent.releaseResource();
        }
    }
}
