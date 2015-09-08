package co.lujun.library.platform.weixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import co.lujun.library.TPManager;
import co.lujun.library.listener.StateListener;
import co.lujun.library.platform.weixin.openapi.WXApiService;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lujun on 2015/9/8.
 */
public class AssistActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mAPI;
    private StateListener<String> mListener;

    private final static String WX_API_HOST = "https://api.weixin.qq.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAPI = WXManager.getWXAPI();
        mListener = WXManager.getStateListener();
        if (mAPI != null){
            mAPI.handleIntent(getIntent(), this);
        }
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (null == mListener){
            mListener.onError("response is null!");
            finish();
        }
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp.getType() == 1){//授权登录获取用户信息
                    getAccessToken(((SendAuth.Resp) baseResp).code);
                }else if (baseResp.getType() == 2){// 分享
                    mListener.onComplete("share successful!");
                }else {
                    mListener.onError("operation type is invalid!");
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                mListener.onCancel();
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                mListener.onError("sent failed!");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                mListener.onError("auth denied!");
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                mListener.onError("operation is not support!");
                break;
            case BaseResp.ErrCode.ERR_COMM:
                mListener.onError("COMM error!");
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mAPI != null){
            mAPI.handleIntent(intent, this);
        }
        finish();
    }

    /***
     * get access token
     * @param code
     */
    private void getAccessToken(String code){
        final RestAdapter mRestAdapter = new RestAdapter.Builder().setEndpoint(WX_API_HOST).build();
        mRestAdapter.create(WXApiService.class)
                .getWXAccessToken(
                        TPManager.getInstance().getWXAppId(),
                        TPManager.getInstance().getWXAppSecret(),
                        code,
                        "authorization_code"
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mListener.onError(e.toString());
                            }

                            @Override
                            public void onNext(Response response) {
                                try {
                                    String jsonStr = new String(((TypedByteArray) response.getBody()).getBytes());
                                    JSONObject jsonObject = new JSONObject(jsonStr);
                                    String accesstoken = jsonObject.getString("access_token");
                                    String openid = jsonObject.getString("openid");
                                    getUserInfo(mRestAdapter, accesstoken, openid);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    mListener.onError(e.toString());
                                }
                            }
                        }
                );
    }

    /**
     * get user information
     * @param restAdapter
     * @param access_token
     * @param openid
     */
    private void getUserInfo(RestAdapter restAdapter, String access_token, String openid){
        restAdapter.create(WXApiService.class)
                .getUserInfo(access_token, openid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mListener.onError(e.toString());
                            }

                            @Override
                            public void onNext(Response response) {
                                String jsonStr = new String(((TypedByteArray) response.getBody()).getBytes());
                                mListener.onComplete(jsonStr);
                            }
                        }
                );
    }
}
