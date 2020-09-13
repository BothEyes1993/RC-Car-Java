package com.zbkj.crmeb.wechat.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SendTemplateMessageVo对象", description="微信模板发送类")
public class TemplateMessageVo {
    @ApiModelProperty(value = "OPENID", required = true)
    private String touser;

    @ApiModelProperty(value = "模板ID", required = true)
    private String template_id;

    @ApiModelProperty(value = "模板跳转链接（海外帐号没有跳转能力）")
    private String url;

    @ApiModelProperty(value = "发送内容")
    private HashMap<String, SendTemplateMessageItemVo> data;
}
