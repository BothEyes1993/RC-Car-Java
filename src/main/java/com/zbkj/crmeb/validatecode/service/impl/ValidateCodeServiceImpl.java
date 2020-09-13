package com.zbkj.crmeb.validatecode.service.impl;

import com.constants.Constants;
import com.exception.CrmebException;
import com.utils.CrmebUtil;
import com.utils.RedisUtil;
import com.utils.ValidateCodeUtil;
import com.zbkj.crmeb.validatecode.model.ValidateCode;
import com.zbkj.crmeb.validatecode.service.ValidateCodeService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
* @author Mr.Zhang
* @Description SystemAdminServiceImpl 接口实现
* @since 2020-04-13
*/
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取验证码信息
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    @Override
    public ValidateCode get() {
        ValidateCodeUtil.Validate randomCode = ValidateCodeUtil.getRandomCode();//直接调用静态方法，返回验证码对象
        if(randomCode == null){
            return null;
        }

        String value = randomCode.getValue().toLowerCase();
        String md5Key = DigestUtils.md5Hex(value);
        String redisKey = getRedisKey(md5Key);
        redisUtil.set(redisKey, value, 5L, TimeUnit.MINUTES);   //5分钟过期
        String base64Str = randomCode.getBase64Str();
        return new ValidateCode(md5Key, CrmebUtil.getBase64Image(base64Str));
    }

    /**
     * 获取redis key
     * @param md5Key value的md5加密值
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    public String getRedisKey(String md5Key){
        return Constants.VALIDATE_REDIS_KEY_PREFIX + md5Key;
    }

    /**
     * 验证
     * @param validateCode
     * @author Mr.Zhang
     * @since 2020-04-16
     */
    public boolean check(ValidateCode validateCode){
        if(!redisUtil.exists(getRedisKey(validateCode.getKey()))) throw new CrmebException("验证码错误");
        Object redisValue = redisUtil.get(getRedisKey(validateCode.getKey()));
        if(!redisValue.equals(validateCode.getCode())){
            return false;
        }
        return true;
    }
}

