package com.zbkj.crmeb.wechat.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微信关键字回复表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_wechat_reply")
@ApiModel(value="WechatReply对象", description="微信关键字回复表")
public class WechatReply implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "微信关键字回复id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "回复类型")
    private String type;

    @ApiModelProperty(value = "回复数据")
    private String data;

    @ApiModelProperty(value = "0=不可用  1 =可用")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}
