package com.zbkj.crmeb.express.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * <p>
 * 
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_shipping_templates_region")
@ApiModel(value="ShippingTemplatesRegion对象", description="")
public class ShippingTemplatesRegion implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "模板ID")
    private Integer tempId;

    @ApiModelProperty(value = "城市ID")
    private Integer cityId;

    @ApiModelProperty(value = "描述")
    private String title;

    @ApiModelProperty(value = "首件")
    private BigDecimal first;

    @ApiModelProperty(value = "首件运费")
    private BigDecimal firstPrice;

    @ApiModelProperty(value = "续件")
    private BigDecimal renewal;

    @ApiModelProperty(value = "续件运费")
    private BigDecimal renewalPrice;

    @ApiModelProperty(value = "计费方式 1按件数 2按重量 3按体积")
    private Integer type;

    @ApiModelProperty(value = "分组唯一值")
    private String uniqid;

    @ApiModelProperty(value = "是否无效")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;


}
