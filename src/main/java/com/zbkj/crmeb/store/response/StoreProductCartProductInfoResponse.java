package com.zbkj.crmeb.store.response;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.zbkj.crmeb.store.model.StoreProductAttrValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StoreProductCartProductInfoResponse对象", description="商品信息，购物车列表使用")
public class StoreProductCartProductInfoResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "商品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)")
    private Integer merId;

    @ApiModelProperty(value = "商品图片")
    private String image;

    @ApiModelProperty(value = "轮播图")
    private String sliderImage;

    @ApiModelProperty(value = "商品名称")
    private String storeName;

    @ApiModelProperty(value = "商品简介")
    private String storeInfo;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "商品条码（一维码）")
    private String barCode;

    @ApiModelProperty(value = "分类id")
    private String cateId;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "会员价格")
    private BigDecimal vipPrice;

    @ApiModelProperty(value = "市场价")
    private BigDecimal otPrice;

    @ApiModelProperty(value = "邮费")
    private BigDecimal postage;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "库存")
    private Integer stock;
//
//    @ApiModelProperty(value = "状态（0：未上架，1：上架）")
//    private Boolean isShow;
//
//    @ApiModelProperty(value = "是否热卖")
//    private Boolean isHot;
//
//    @ApiModelProperty(value = "是否优惠")
//    private Boolean isBenefit;
//
//    @ApiModelProperty(value = "是否精品")
//    private Boolean isBest;
//
//    @ApiModelProperty(value = "是否新品")
//    private Boolean isNew;
//
//    @ApiModelProperty(value = "添加时间")
//    private Integer addTime;

    @ApiModelProperty(value = "是否包邮")
    private Boolean isPostage;

//    @ApiModelProperty(value = "是否删除")
//    private Boolean isDel;
//
//    @ApiModelProperty(value = "商户是否代理 0不可代理1可代理")
//    private Boolean merUse;

    @ApiModelProperty(value = "获得积分")
    private Integer giveIntegral;

    @ApiModelProperty(value = "成本价")
    private BigDecimal cost;

//    @ApiModelProperty(value = "秒杀状态 0 未开启 1已开启")
//    private Boolean isSeckill;
//
//    @ApiModelProperty(value = "砍价状态 0未开启 1开启")
//    private Boolean isBargain;
//
//    @ApiModelProperty(value = "是否优品推荐")
//    private Boolean isGood;

    @ApiModelProperty(value = "是否单独分佣")
    private Boolean isSub;

//    @ApiModelProperty(value = "虚拟销量")˚
//    private Integer ficti;
//
//    @ApiModelProperty(value = "浏览量")
//    private Integer browse;
//
//    @ApiModelProperty(value = "商品二维码地址(用户小程序海报)")
//    private String codePath;
//
//    @ApiModelProperty(value = "淘宝京东1688类型")
//    private String soureLink;
//
//    @ApiModelProperty(value = "主图视频链接")
//    private String videoLink;

    @ApiModelProperty(value = "运费模板ID")
    private Integer tempId;

//    @ApiModelProperty(value = "规格 0单 1多")
//    private Boolean specType;
//
//    @ApiModelProperty(value = "活动显示排序1=秒杀，2=砍价，3=拼团")
//    private String activity;

    @ApiModelProperty(value = "sku详情")
    private StoreProductAttrValue attrInfo;
}
