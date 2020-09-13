package com.zbkj.crmeb.marketing.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-05-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_coupon")
@ApiModel(value="StoreCoupon对象", description="优惠券表")
public class StoreCouponFrontResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "优惠券表ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "兑换的优惠券面值")
    private BigDecimal money;

    @ApiModelProperty(value = "是否限量, 默认0 不限量， 1限量")
    private Boolean isLimited;

    @ApiModelProperty(value = "发放总数")
    private Integer total;

    @ApiModelProperty(value = "剩余数量")
    private Integer lastTotal;

    @ApiModelProperty(value = "所属商品id / 分类id")
    private String primaryKey;

    @ApiModelProperty(value = "最低消费，0代表不限制")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "可领取开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date receiveStartTime;

    @ApiModelProperty(value = "可领取结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date receiveEndTime;

    @ApiModelProperty(value = "是否固定使用时间, 默认0 否， 1是")
    private Boolean isFixedTime;

    @ApiModelProperty(value = "可使用时间范围 开始时间")
    private Date useStartTime;

    @ApiModelProperty(value = "可使用时间范围 结束时间")
    private Date useEndTime;

    @ApiModelProperty(value = "天数")
    private Integer day;

    @ApiModelProperty(value = "优惠券类型 0-通用 1 普通券, 2 新人券, 3 购买商品赠送券, 4 付费会员券")
    private Integer type;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态（0：关闭，1：开启）")
    private Boolean status;

    @ApiModelProperty(value = "是否删除 状态（0：否，1：是）")
    private Boolean isDel;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否已领取未使用")
    private Boolean isUse = false;
}
