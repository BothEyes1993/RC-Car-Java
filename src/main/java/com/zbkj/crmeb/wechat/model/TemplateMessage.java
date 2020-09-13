package com.zbkj.crmeb.wechat.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 微信模板
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_template_message")
@ApiModel(value="TemplateMessage对象", description="微信模板")
public class TemplateMessage implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "模板id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "0=订阅消息,1=微信模板消息")
    private Boolean type;

    @ApiModelProperty(value = "模板编号")
    private String tempKey;

    @ApiModelProperty(value = "模板名")
    @JsonProperty
    private String name;

    @ApiModelProperty(value = "回复内容")
    private String content;

    @ApiModelProperty(value = "模板ID")
    private String tempId;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
