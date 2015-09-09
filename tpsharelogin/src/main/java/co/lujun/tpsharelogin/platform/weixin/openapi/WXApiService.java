package co.lujun.tpsharelogin.platform.weixin.openapi;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lujun on 2015/9/8.
 */
public interface WXApiService {

    @GET("/sns/oauth2/access_token")
    Observable<Response> getWXAccessToken(
            @Query("appid") String appId,
            @Query("secret") String secret,
            @Query("code") String code,
            @Query("grant_type") String grant_type
    );

    @GET("/sns/userinfo")
    Observable<Response> getUserInfo(
            @Query("access_token") String access_token,
            @Query("openid") String openid
    );
}
