package com.zbkj.crmeb.authorization.model;

import com.utils.CrmebUtil;
import com.zbkj.crmeb.system.model.SystemAdmin;
import lombok.Data;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author stivepeim
 * @title: TokenModel
 * @projectName crmeb
 * @Description: Token实体类
 * @since 2020/4/1415:00
 */
@Data
public class TokenModel implements Serializable {
    // 加密后的token key
    public static final String TOKEN_KEY = "TOKEN";
    // Redis 存储的key
    public static final String TOKEN_REDIS = "TOKEN_ADMIN_";
    // 用户号
    private String userNo;
    private Integer userId;
    private String Token;
    // 最后访问时间
    private long lastAccessedTime = System.currentTimeMillis();
    // 过期时间
    private long expirationTime;
    // 客户端类型
    private String clienttype;
    // 客户端语言
    private Locale locale;
    // 客户端ip
    private String host;
    // 当前登录用户信息
    private SystemAdmin systemAdmin;

    public String getAuthorization() throws Exception {
        return CrmebUtil.encryptPassword(userNo+"_"+Token, TOKEN_KEY);
    }

    public TokenModel(String userno, String token){
        this.userNo = userno;
        this.Token = token;
    }

    public TokenModel() {
    }
}
