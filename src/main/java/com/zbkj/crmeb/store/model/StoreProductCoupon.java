package com.zbkj.crmeb.store.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_product_coupon")
@ApiModel(value="StoreProductCoupon对象", description="")
public class StoreProductCoupon implements Serializable {

    private static final long serialVersionUID=1L;

    public StoreProductCoupon() {
    }

    public StoreProductCoupon(Integer productId, Integer issueCouponId, Integer addTime) {
        this.productId = productId;
        this.issueCouponId = issueCouponId;
        this.addTime = addTime;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品id")
    private Integer productId;

    @ApiModelProperty(value = "优惠劵id")
    private Integer issueCouponId;

    @ApiModelProperty(value = "添加时间")
    private Integer addTime;


}
