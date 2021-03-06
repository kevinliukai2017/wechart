package com.wechart.req.weixin;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.WeiXinReqService;
import org.jeewx.api.core.req.model.AccessToken;
import org.jeewx.api.core.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wechart.model.webaccesstoken.AccessTokenModel;
import com.wechart.model.webaccesstoken.JSAPITokenModel;
import com.wechart.model.webaccesstoken.WebAccessTokenModel;
import com.wechart.service.webaccesstoken.WebAccessTokenService;

import net.sf.json.JSONObject;

public class WebAuthAPI {

    // accesstoken 调用微信接口使用
    private static AccessTokenModel accessToken = null;
    // jsapitoken jssdk使用
    private static JSAPITokenModel jsapiToken = null;
    // 最新获取accesstoken的时间
    private static long lastAccessTokenRequestTime = System.currentTimeMillis();
    // 最新获取jsapitoken时间
    private static long lastJSAPITokenRequestTime = System.currentTimeMillis();

    private static final Logger logger = LoggerFactory.getLogger(WebAuthAPI.class);

    private WebAuthAPI() {

    }

    /**
     * 获取 access_token
     * 
     * @param appid
     * @param secret
     * @return
     * @throws WexinReqException
     */
    public static AccessTokenModel getAccessToken(String appid, String secret) throws WexinReqException {
        int runningTime = DateUtils.dateDiff('s', DateUtils.getCalendar(),
                DateUtils.getCalendar(lastAccessTokenRequestTime));
        if (null == accessToken || runningTime > 7200) {
            logger.info("access_token为null或者已经失效(" + runningTime + "),生成新的access_token--------------------");
            // 同步块，线程安全的创建实例
            synchronized (AccessTokenModel.class) {
                AccessToken atoken = new AccessToken();
                atoken.setAppid(appid);
                atoken.setSecret(secret);
                JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(atoken);
                logger.info(result.toString());
                accessToken = (AccessTokenModel) result.toBean(result, AccessTokenModel.class);
                logger.info("access_token:" + accessToken.getAccess_token());
                lastAccessTokenRequestTime = System.currentTimeMillis();
            }
        }
        return accessToken;
    }

    /**
     * 通过code换取网页授权web_access_token
     * 
     * @param appid
     * @param secret
     * @param code
     * @param grantType
     * @return
     * @throws WexinReqException
     */
    public static WebAccessTokenModel getWebAccessToken(String appid, String secret, String code, String grantType)
            throws WexinReqException {
        logger.info("生成web_access_token-----------------------------");
        logger.info("appid:" + appid);
        logger.info("secret:" + secret);
        logger.info("code:" + code);
        logger.info("grantType:" + grantType);
        GetWebAccessToken parm = new GetWebAccessToken();
        parm.setAppid(appid);
        parm.setSecret(secret);
        parm.setCode(code);
        parm.setGrant_type(grantType);
        JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(parm);
        logger.info("web_access_token is generated------------------------------");
        logger.info(result.toString());
        return (WebAccessTokenModel) result.toBean(result, WebAccessTokenModel.class);
    }

    /**
     * 获取js api token
     * 
     * @param accessToken
     * @return
     * @throws WexinReqException
     */
    public static JSAPITokenModel getJSAPIToken(String accessToken) throws WexinReqException {
        int runningTime = DateUtils.dateDiff('s', DateUtils.getCalendar(),
                DateUtils.getCalendar(lastJSAPITokenRequestTime));
        if (null == jsapiToken || runningTime > 7200) {
            logger.info("jspApiToekn为null或者已经失效(" + runningTime + "),生成新的jspapitoken--------------------");
            // 同步块，线程安全的创建实例
            synchronized (WebAccessTokenModel.class) {
                GetJSAPIToken parm = new GetJSAPIToken();
                parm.setAccess_token(accessToken);
                JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(parm);
                logger.info(result.toString());
                jsapiToken = (JSAPITokenModel) result.toBean(result, JSAPITokenModel.class);
                logger.info("jsapi_token:" + jsapiToken.getTicket());
                lastJSAPITokenRequestTime = System.currentTimeMillis();
            }
        }
        return jsapiToken;
    }


    public static long getLastAccessTokenRequestTime() {
        return lastAccessTokenRequestTime;
    }

    public static long getLastJSAPITokenRequestTime() {
        return lastJSAPITokenRequestTime;
    }

}
