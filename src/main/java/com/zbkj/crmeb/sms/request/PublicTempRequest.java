package com.zbkj.crmeb.sms.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 短信发送记录表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="RegisterRequest对象", description="短信账号注册")
public class PublicTempRequest implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "账号", required = true)
    private String accountId;

    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @ApiModelProperty(value = "域名", required = true)
    private String domain;

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty(value = "签名", required = true)
    private String sign;

    @ApiModelProperty(value = "验证码")
    private String code;


}
