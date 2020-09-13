package com.zbkj.crmeb.store.model;

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
 * 购物车表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_cart")
@ApiModel(value="StoreCart对象", description="购物车表")
public class StoreCart implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "购物车表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "商品ID")
    private Integer productId;

    @ApiModelProperty(value = "商品属性")
    private String productAttrUnique;

    @ApiModelProperty(value = "商品数量")
    private Integer cartNum;

    @ApiModelProperty(value = "是否为立即购买")
    private Boolean isNew;

    @ApiModelProperty(value = "拼团id")
    private Integer combinationId;

    @ApiModelProperty(value = "秒杀商品ID")
    private Integer seckillId;

    @ApiModelProperty(value = "砍价id")
    private Integer bargainId;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "已添加的商品是否有效状态")
    private Boolean status;
}
