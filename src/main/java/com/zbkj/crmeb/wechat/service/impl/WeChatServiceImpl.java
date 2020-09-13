package com.zbkj.crmeb.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.constants.Constants;
import com.constants.WeChatConstants;
import com.exception.CrmebException;
import com.utils.CrmebUtil;
import com.utils.DateUtil;
import com.utils.RedisUtil;
import com.utils.RestTemplateUtil;
import com.zbkj.crmeb.front.response.UserRechargePaymentResponse;
import com.zbkj.crmeb.payment.vo.wechat.CreateOrderResponseVo;
import com.zbkj.crmeb.system.service.SystemConfigService;
import com.zbkj.crmeb.wechat.response.WeChatAuthorizeLoginGetOpenIdResponse;
import com.zbkj.crmeb.wechat.response.WeChatAuthorizeLoginUserInfoResponse;
import com.zbkj.crmeb.wechat.response.WeChatProgramAuthorizeLoginGetOpenIdResponse;
import com.zbkj.crmeb.wechat.service.WeChatService;
import com.zbkj.crmeb.wechat.vo.MediaCountVo;
import com.zbkj.crmeb.wechat.vo.TemplateMessageVo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
* @author Mr.Zhang
* @Description WeChatPublicServiceImpl 接口实现
* @since 2020-04-22
*/
@Service
public class WeChatServiceImpl implements WeChatService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    private Object token; //token

    private String appId; //appId

    private String secret; //秘钥

    private Long expires; //过期时间

    private String url; //url

    private String uuid; //uuid

    private int timestamp; //时间戳

    private String signature; //签名

    private String jsApiTicket; // jsApiTicket

    private String programAppId; // 小程序appId

    private String programAppSecret; //小程序秘钥

    private Object programToken; //小程序token

    private Long programExpires; //小程序过期时间


    @Value("${server.wechat-api-url}")
    private String weChartApiUrl;

    @Value("${server.wechat-js-api-debug}")
    private Boolean debug;

    @Value("${server.wechat-js-api-beta}")
    private Boolean beta;

    private String getUuid() {
        return uuid;
    }

    /**
     * 设置uuid
     * @author Mr.Zhang
     * @since 2020-06-22
     */
    private void setUuid() {
        this.uuid = CrmebUtil.getUuid();
    }

    private int getTimestamp() {
        return timestamp;
    }

    /**
     * 设置13位时间戳
     * @author Mr.Zhang
     * @since 2020-06-22
     */
    private void setTimestamp() {
        this.timestamp = DateUtil.getSecondTimestamp();
    }

    /**
     * 获取签名
     * @author Mr.Zhang
     * @since 2020-06-22
     * @return String
     */
    private String getSignature() {
        return signature;
    }

    /**
     * 获取jsApiTicket
     * @author Mr.Zhang
     * @since 2020-06-22
     * @return String
     */
    private String getJsApiTicket(){
        return jsApiTicket;
    }


    /**
     * 设置token
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private void setToken(){
        //检测token是否过期， 如果过期，那么重新获取
        Object token = redisUtil.get(WeChatConstants.REDIS_TOKEN_KEY);
        if(token == null || token.equals("")){
            //不存在， 去获取
            getToken();
            //存入redis
            if(StringUtils.isBlank(weChartApiUrl)){
                redisUtil.set(WeChatConstants.REDIS_TOKEN_KEY, this.token, this.expires - 100, TimeUnit.SECONDS);
            }
        }else{
            this.token = token;
        }
    }


    /**
    * 获取token
    * @author Mr.Zhang
    * @since 2020-04-22
    */
    private void getToken(){
        getAppInfo();
        String url = getUrl() + WeChatConstants.API_TOKEN_URI + "&appid=" + this.appId + "&secret=" + this.secret;
        JSONObject data = get(url);
        if(!data.containsKey("access_token")){
            throw new CrmebException("微信token获取失败：" + data.getString("errmsg"));
        }

        this.token = data.getString("access_token");
        this.expires = Long.valueOf(data.getString("expires_in"));
    }

    /**
     * 设置token
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private void setProgramToken(){
        //检测token是否过期， 如果过期，那么重新获取
        Object token = redisUtil.get(WeChatConstants.REDIS_PROGRAM_TOKEN_KEY);
        if(token == null || token.equals("")){
            //不存在， 去获取
            getProgramToken();
            //存入redis
            if(StringUtils.isBlank(weChartApiUrl)){
                redisUtil.set(WeChatConstants.REDIS_PROGRAM_TOKEN_KEY, this.programToken, this.programExpires - 100, TimeUnit.SECONDS);
            }
        }else{
            this.programToken = token;
        }
    }

    /**
     * 获取token
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private void getProgramToken(){
        getProgramAppInfo();
        String url = getUrl() + WeChatConstants.API_TOKEN_URI + "&appid=" + this.programAppId + "&secret=" + this.programAppSecret;
        JSONObject data = get(url);
        if(!data.containsKey("access_token")){
            throw new CrmebException("微信token获取失败：" + data.getString("errmsg"));
        }

        this.programToken = data.getString("access_token");
        this.programExpires = Long.valueOf(data.getString("expires_in"));
    }

    /**
     * 设置url
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private String getUrl(){
        String apiUrl = WeChatConstants.API_URL;
        if(StringUtils.isNotBlank(weChartApiUrl)){
            //如果有配置开发url则取开发url
            apiUrl = weChartApiUrl;
        }
        return apiUrl;
    }


    /**
     * 设置url
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private void setUrl(String uri){
        setToken();
        this.url = getUrl() + uri + "?access_token=" + this.token;
    }

    /**
     * 设置url
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private void setProgramUrl(String uri){
        setProgramToken();
        this.url = getUrl() + uri + "?access_token=" + this.programToken;
    }

    /**
     * 获取appInfo配置信息
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private void getAppInfo() {
        this.appId = systemConfigService.getValueByKey("wechat_appid");
        this.secret = systemConfigService.getValueByKey("wechat_appsecret");

        if(StringUtils.isBlank(this.appId)){
            throw new CrmebException("微信appId未设置");
        }

        if(StringUtils.isBlank(this.secret)){
            throw new CrmebException("微信secret未设置");
        }
    }

    /**
     * 获取小程序appInfo配置信息
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    private void getProgramAppInfo() {
        this.programAppId = systemConfigService.getValueByKey("routine_appid");
        this.programAppSecret = systemConfigService.getValueByKey("routine_appsecret");

        if(StringUtils.isBlank(this.programAppId)){
            throw new CrmebException("微信小程序appId未设置");
        }

        if(StringUtils.isBlank(this.programAppSecret)){
            throw new CrmebException("微信小程序secret未设置");
        }
    }


    /**
     * post 请求接口
     * @param url 请求url
     * @param data json格式参数
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    private JSONObject post(String url, String data){
        JSONObject jsonData = JSONObject.parseObject(data);
        String result = restTemplateUtil.postJsonData(url, jsonData);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return checkResult(jsonObject);
    }

    /**
     * post 请求接口
     * @param url 请求url
     * @param data json格式参数
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    private JSONObject post(String url, HashMap<String, Object> data){
        try{
            String result = restTemplateUtil.postJsonData(url, new JSONObject(data));
            JSONObject jsonObject = JSONObject.parseObject(result);
            return checkResult(jsonObject);
        }catch (Exception e){
            throw new CrmebException(e.getMessage());
        }
    }

    /**
     * post 请求接口
     * @param url 请求url
     * @param data json格式参数
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    private String postReturnBuffer(String url, HashMap<String, Object> data){
        try{
            byte[] bytes = restTemplateUtil.postJsonDataAndReturnBuffer(url, new JSONObject(data));
            if(null == bytes){
                throw new CrmebException("微信生成二维码异常");
            }

            return CrmebUtil.getBase64Image(Base64.encodeBase64String(bytes));

        }catch (Exception e){
            throw new CrmebException(e.getMessage());
        }

    }


    /**
     * get 请求接口
     * @param url 请求url
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    private JSONObject get(String url){
        try{
            JSONObject result = restTemplateUtil.getData(url);
            return checkResult(result);
        }catch (Exception e){
            throw new CrmebException(e.getMessage());
        }

    }

    /**
     * 检测结构请求返回的数据
     * @param result 接口返回的结果
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    private JSONObject checkResult(JSONObject result){

        if(result.equals("")){
            throw new CrmebException("微信平台接口异常，没任何数据返回！");
        }


        if(result.containsKey("errcode") && result.getString("errcode").equals("0")){
            return result;
        }

        if(result.containsKey("errmsg")){
            throw new CrmebException("微信接口调用失败：" + result.getString("errcode") + result.getString("errmsg"));
        }

        return result;
    }

    /**
     * 创建菜单
     * @param data 菜单数据
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject create(String data) {
        setUrl(WeChatConstants.PUBLIC_API_MENU_CREATE_URI);
        return post(this.url, data);
    }

    /**
     * 获取菜单
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject get() {
        setUrl(WeChatConstants.PUBLIC_API_MENU_GET_URI);
        return get(this.url);
    }

    /**
     * 使用接口创建自定义菜单后，开发者还可使用接口删除当前使用的自定义菜单。另请注意，在个性化菜单时，调用此接口会删除默认菜单及全部个性化菜单。
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject delete() {
        setUrl(WeChatConstants.PUBLIC_API_MENU_DELETE_URI);
        return get(this.url);
    }

    /**
     * 自定义菜单，获取。
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject getSelf() {
        setUrl(WeChatConstants.PUBLIC_API_MENU_SELF_SET_URI);
        return get(this.url);
    }

    /**
     * 自定义菜单，创建。
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject createSelf(String data) {
        setUrl(WeChatConstants.PUBLIC_API_MENU_ADD_CONDITIONAL_URI);
        return post(this.url, data);
    }

    /**
     * 自定义菜单，删除。
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject deleteSelf(String menuId) {
        setUrl(WeChatConstants.PUBLIC_API_MENU_ADD_CONDITIONAL_URI);
        return post(this.url, "{menuid:"+menuId+"}");
    }

    /**
     * 公众号图文消息推送
     * @author Mr.Zhang
     * @since 2020-04-22
     */
    @Async
    @Override
    public void pushKfMessage(HashMap<String, Object> map) {
        setUrl(WeChatConstants.PUBLIC_API_KF_MESSAGE_SEND);
        post(this.url, map);
    }

    /**
     * 用户管理 创建标签
     * @param name 标签名
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject {"tag":{"id":134,//标签id "name":"广东"}}
     */
    @Override
    public JSONObject createTags(String name) {
        setUrl(WeChatConstants.PUBLIC_API_TAG_CREATE_URI);
        return post(this.url, "{\"tag\":{\"name\":\""+name+"\"}}");
    }

    /**
     * 用户管理 获取标签列表
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject getTagsList() {
        setUrl(WeChatConstants.PUBLIC_API_TAG_LIST_URI);
        return get(this.url);
    }

    /**
     * 用户管理 修改标签
     * @param name 标签名
     * @param id 标签id
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject {"errcode":0,"errmsg":"ok" }
     */
    @Override
    public JSONObject updateTags(String id, String name) {
        setUrl(WeChatConstants.PUBLIC_API_TAG_CREATE_URI);
        return post(this.url, "{\"tag\":{\"id\":"+id+",\"name\":\""+name+"\"}}");
    }

    /**
     * 用户管理 删除标签
     * @param id 标签id
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject {"errcode":0,"errmsg":"ok" }
     */
    @Override
    public JSONObject deleteTags(String id) {
        setUrl(WeChatConstants.PUBLIC_API_TAG_DELETE_URI);
        return post(this.url, "{\"tag\":{\"id\":"+id+"}}");
    }

    /**
     * 用户管理 获取标签下粉丝列表
     * @param id 标签id
     * @param nextOpenId 第一个拉取的OPENID，不填默认从头开始拉取
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject getUserListByTagsId(String id, String nextOpenId) {
        setUrl(WeChatConstants.PUBLIC_API_TAG_USER_GET_URI);
        HashMap<String, Object> map = new HashMap<>();
        map.put("tagid", id);
        map.put("next_openid", nextOpenId);
        return post(this.url, map);
    }

    /**
     * 用户管理 批量为用户取消标签
     * @param id 标签id
     * @param data 粉丝list
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject {"errcode":0,"errmsg":"ok" }
     */
    @Override
    public JSONObject memberBatchTags(String id, String data) {
        setUrl(WeChatConstants.PUBLIC_API_TAG_MEMBER_BATCH_URI);
        HashMap<String, Object> map = new HashMap<>();
        map.put("openid_list", CrmebUtil.stringToArrayStr(data));
        map.put("tagid", id);
        return post(this.url, map);
    }

    /**
     * 用户管理 批量为用户取消标签
     * @param id 标签id
     * @param data 粉丝list
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject {"errcode":0,"errmsg":"ok" }
     */
    @Override
    public JSONObject memberBatchUnTags(String id, String data) {
        setUrl(WeChatConstants.PUBLIC_API_TAG_MEMBER_BATCH_UN_URI);
        HashMap<String, Object> map = new HashMap<>();
        map.put("openid_list", CrmebUtil.stringToArrayStr(data));
        map.put("tagid", id);
        return post(this.url, map);
    }

    /**
     * 用户管理 获取用户身上的标签列表
     * @param openId 用户身份id
     * @author Mr.Zhang
     * @since 2020-04-22
     * @return JSONObject
     */
    @Override
    public JSONObject getTagsListByUserId(String openId) {
        setUrl(WeChatConstants.PUBLIC_API_TAG_GET_ID_LIST_URI);
        HashMap<String, Object> map = new HashMap<>();
        map.put("openid", openId);
        return post(this.url, map);
    }

    /**
     * 获取授权页面跳转地址
     * @author Mr.Zhang
     * @since 2020-05-25
     * @return String
     */
    @Override
    public String getAuthorizeUrl() {
        getAppInfo();
        String redirectUri = systemConfigService.getValueByKeyException(Constants.CONFIG_KEY_SITE_URL) + WeChatConstants.WE_CHAT_AUTHORIZE_REDIRECT_URI_URL;
        return WeChatConstants.WE_CHAT_AUTHORIZE_URL.replace("{$appId}", this.appId).replace("{$redirectUri}", redirectUri);
    }

    /**
     * 通过code获取获取公众号授权信息
     * @author Mr.Zhang
     * @since 2020-05-25
     * @return LoginResponse
     */
    @Override
    public WeChatAuthorizeLoginGetOpenIdResponse authorizeLogin(String code) {
        //通过code获取access_token
        try {
            getAppInfo();

            String url = getUrl() + WeChatConstants.WE_CHAT_AUTHORIZE_GET_OPEN_ID +
                    "?appid=" + this.appId +
                    "&secret=" + this.secret +
                    "&code=" + code +
                    "&grant_type=authorization_code";
            JSONObject result = get(url);
            return JSONObject.parseObject(result.toJSONString(), WeChatAuthorizeLoginGetOpenIdResponse.class);
        }catch (Exception e){
            throw new CrmebException(e.getMessage());
        }
    }

    /**
     * 通过code获取获取小程序授权信息
     * @author Mr.Zhang
     * @since 2020-05-25
     * @return LoginResponse
     */
    @Override
    public WeChatProgramAuthorizeLoginGetOpenIdResponse programAuthorizeLogin(String code) {
        //通过code获取access_token
        try {
            getProgramAppInfo();
            String url = getUrl() + WeChatConstants.WE_CHAT_AUTHORIZE_PROGRAM_GET_OPEN_ID +
                    "?appid=" + this.programAppId +
                    "&secret=" + this.programAppSecret +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";
            JSONObject result = get(url);
            return JSONObject.parseObject(result.toJSONString(), WeChatProgramAuthorizeLoginGetOpenIdResponse.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new CrmebException(e.getMessage());
        }
    }

    @Override
    public String qrCode(String page, String uri) {
        setProgramUrl(WeChatConstants.WE_CHAT_CREATE_QRCODE);
        HashMap<String, Object> map = new HashMap<>();
        map.put("scene", uri);
        map.put("page", page);
        map.put("width", 200);
        return postReturnBuffer(this.url, map);
    }

    @Override
    public UserRechargePaymentResponse response(CreateOrderResponseVo responseVo) {
        UserRechargePaymentResponse response = new UserRechargePaymentResponse();
        response.setAppId(responseVo.getAppId());
        response.setNonceStr(responseVo.getNonceStr());

        String prepay = "prepay_id=" + responseVo.getPrepayId();

        //构建小程序支付签名
        HashMap<String, Object> map = new HashMap<>();
        map.put("appId", responseVo.getAppId());
        map.put("timeStamp", response.getTimeStamp());
        map.put("nonceStr", response.getNonceStr());
        map.put("package", prepay);
        map.put("signType", response.getSignType());

        //重新计算签名
        response.setPaySign(CrmebUtil.getSign(map, systemConfigService.getValueByKey(Constants.CONFIG_KEY_PAY_WE_CHAT_APP_KEY)));

        response.setExtra(prepay);
        response.setPrepayId(responseVo.getPrepayId());
        response.setH5PayUrl(responseVo.getMWebUrl()); //H5支付会有值，否则为null
        return response;
    }


    /**
     * 获取微信用户个人信息
     * @param openId String code换取的 openId
     * @param token String code换取的token
     * @author Mr.Zhang
     * @since 2020-06-01
     * @return WeChatAuthorizeLoginUserInfoResponse
     */
    @Override
    public WeChatAuthorizeLoginUserInfoResponse getUserInfo(String openId, String token) {
        String url = getUrl() + WeChatConstants.WE_CHAT_AUTHORIZE_GET_USER_INFO +
                "?access_token=" + token +
                "&openid=" + openId +
                "&lang=zh_CN";
        JSONObject result = get(url);
        return JSONObject.parseObject(result.toJSONString(), WeChatAuthorizeLoginUserInfoResponse.class);
    }

    /**
     * 获取微信公众号js配置
     * @author Mr.Zhang
     * @since 2020-06-01
     * @return Object
     */
    @Override
    public Object getJsSdkConfig(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CrmebException("url无法解析！");
        }
        setJsApiTicket();
        setJsSDKSign(url);

        Map<Object, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("appId", this.appId);
        map.put("jsApiTicket", getJsApiTicket());
        map.put("nonceStr", getUuid());
        map.put("timestamp", getTimestamp());
        map.put("signature", getSignature());
        map.put("jsApiList", getJsApiList());
        map.put("debug", debug);
        map.put("beta", beta);
        return map;
    }


    /**
     * 使用access_token获取jsapi_ticket
     * @author Mr.Zhang
     * @since 2020-06-03
     */
    public void setJsApiTicket() {
        String key = WeChatConstants.REDIS_PUBLIC_JS_API_TICKET;
        Object ticketObject = redisUtil.get(key);
        if(ticketObject != null && !ticketObject.equals("")){
            this.jsApiTicket = ticketObject.toString();
            return;
        }

        setUrl(WeChatConstants.PUBLIC_API_JS_API_TICKET);
        JSONObject jsonObject = get(this.url + "&type=jsapi");
        this.jsApiTicket = jsonObject.getString("ticket");
        redisUtil.set(key, this.jsApiTicket, WeChatConstants.REDIS_PUBLIC_JS_API_TICKET_EXPRESS, TimeUnit.SECONDS);
    }

    /**
     * 获取JsSDK签名
     * @author Mr.Zhang
     * @since 2020-06-01
     */
    private void setJsSDKSign(String url) {
        getAppInfo();
        setUuid();
        setTimestamp();
        String paramString;

        //注意这里参数名必须全部小写，且必须有序
        paramString = "jsapi_ticket=" + getJsApiTicket() +
                "&noncestr=" + getUuid() +
                "&timestamp=" + getTimestamp() +
                "&url=" + url;
        try {

            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            try {
                crypt.update(paramString.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new CrmebException("获取JS SDK配置失败" + e.getMessage());
            }
            this.signature = CrmebUtil.byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new CrmebException("获取JS SDK配置失败" + e.getMessage());
        }
    }

    /**
     * 获取JsSDK 接口权限列表
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return List<String>
     */
    private List<String> getJsApiList() {
        return CrmebUtil.stringToArrayStr(WeChatConstants.PUBLIC_API_JS_API_SDK_LIST);
    }

    /**
     * 模板消息发送
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return List<String>
     */
    public boolean sendPublicTempMessage(TemplateMessageVo templateMessageVo) {
        try{
            setUrl(WeChatConstants.PUBLIC_API__PUBLIC_TEMPLATE_MESSAGE_SEND);
            post(this.url, JSONObject.toJSONString(templateMessageVo));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 模板消息发送
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return List<String>
     */
    public boolean sendProgramTempMessage(TemplateMessageVo templateMessageVo) {
        try{
            setUrl(WeChatConstants.PUBLIC_API__PROGRAM_TEMPLATE_MESSAGE_SEND);
            post(this.url, JSONObject.toJSONString(templateMessageVo));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 模板消息 行业信息
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return List<String>
     */
    @Override
    public JSONObject getIndustry() {
        try{
            setUrl(WeChatConstants.PUBLIC_API_TEMPLATE_MESSAGE_INDUSTRY);
            return get(this.url);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取微信素材上传url
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return String
     */
    @Override
    public String getUploadMedia() {
        setUrl(WeChatConstants.PUBLIC_API_MEDIA_UPLOAD);
        return this.url;
    }

    /**
     * 获取微信素材
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return String
     */
    @Override
    public String getMedia() {
        setUrl(WeChatConstants.PUBLIC_API_MEDIA_GET);
        return this.url;
    }

    /**
     * 获取微信素材
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return String
     */
    public JSONObject getMediaInfo(String type, int offset, int count) {
        setUrl(WeChatConstants.PUBLIC_API_MEDIA_GET);
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);  //素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
        map.put("offset", offset); //从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
        map.put("count", count); //返回素材的数量，取值在1到20之间
        return post(this.url, map);
    }

    /**
     * 获取微信素材总数
     * @author Mr.Zhang
     * @since 2020-06-03
     * @return int
     */
    public int getMediaCount(String type) {
        setUrl(WeChatConstants.PUBLIC_API_MEDIA_COUNT);
        JSONObject jsonObject = get(this.url);
        MediaCountVo mediaCountVo = JSONObject.toJavaObject(jsonObject, MediaCountVo.class);
        switch (type){
            case "image":
                return mediaCountVo.getImage();
            case "voice":
                return mediaCountVo.getVideo();
            default:
                return 0;
        }
    }
}

