package com.zbkj.crmeb.store.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *H5 订单列表Response
 * @author stivepeim
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StoreOrderListResponse对象", description="H5订单列表")
public class StoreOrderListResponse implements Serializable {
    // todo 待删除
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "详细地址")
    private String userAddress;

    @ApiModelProperty(value = "运费金额")
    private BigDecimal freightPrice;

    @ApiModelProperty(value = "订单商品总数")
    private Integer totalNum;

    @ApiModelProperty(value = "订单总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "邮费")
    private BigDecimal totalPostage;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;

    @ApiModelProperty(value = "支付邮费")
    private BigDecimal payPostage;

    @ApiModelProperty(value = "抵扣金额")
    private BigDecimal deductionPrice;

    @ApiModelProperty(value = "优惠券id")
    private Integer couponId;

    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponPrice;

    @ApiModelProperty(value = "支付状态")
    private Boolean paid;

    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    @ApiModelProperty(value = "支付方式")
    private String payType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "订单状态（-1 : 申请退款 -2 : 退货成功 0：待发货；1：待收货；2：已收货，待评价；3：已完成；）")
    private Integer status;

    @ApiModelProperty(value = "0 未退款 1 申请中 2 已退款")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款图片")
    private String refundReasonWapImg;

    @ApiModelProperty(value = "退款用户说明")
    private String refundReasonWapExplain;

    @ApiModelProperty(value = "退款时间")
    private Date refundReasonTime;

    @ApiModelProperty(value = "前台退款原因")
    private String refundReasonWap;

    @ApiModelProperty(value = "不退款的理由")
    private String refundReason;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    @ApiModelProperty(value = "快递名称/送货人姓名")
    private String deliveryName;

    @ApiModelProperty(value = "发货类型")
    private String deliveryType;

    @ApiModelProperty(value = "快递单号/手机号")
    private String deliveryId;

    @ApiModelProperty(value = "消费赚取积分")
    private BigDecimal gainIntegral;

    @ApiModelProperty(value = "使用积分")
    private BigDecimal useIntegral;

    @ApiModelProperty(value = "给用户退了多少积分")
    private BigDecimal backIntegral;

    @ApiModelProperty(value = "备注")
    private String mark;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;

    @ApiModelProperty(value = "唯一id(md5加密)类似id")
    @TableField(value = "`unique`")
    private String unique;

    @ApiModelProperty(value = "管理员备注")
    private String remark;

    @ApiModelProperty(value = "商户ID")
    private Integer merId;

    private Integer isMerCheck;

    @ApiModelProperty(value = "拼团商品id0一般商品")
    private Integer combinationId;

    @ApiModelProperty(value = "拼团id 0没有拼团")
    private Integer pinkId;

    @ApiModelProperty(value = "成本价")
    private BigDecimal cost;

    @ApiModelProperty(value = "秒杀商品ID")
    private Integer seckillId;

    @ApiModelProperty(value = "砍价id")
    private Integer bargainId;

    @ApiModelProperty(value = "核销码")
    private String verifyCode;

    @ApiModelProperty(value = "门店id")
    private Integer storeId;

    @ApiModelProperty(value = "配送方式 1=快递 ，2=门店自提")
    private Integer shippingType;

    @ApiModelProperty(value = "店员id")
    private Integer clerkId;

    @ApiModelProperty(value = "支付渠道(0微信公众号1微信小程序)")
    private int isChannel;

    @ApiModelProperty(value = "消息提醒")
    private Boolean isRemind;

    @ApiModelProperty(value = "后台是否删除")
    private Boolean isSystemDel;

    @ApiModelProperty(value = "购物车信息")
    private List<StoreCartResponse> cartInfo;
}
