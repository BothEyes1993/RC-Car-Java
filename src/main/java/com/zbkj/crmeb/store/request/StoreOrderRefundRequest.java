package com.zbkj.crmeb.store.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * @author stivepeim
 * @title: StoreOrderStatusRequest
 * @projectName crmeb
 * @Description: TODO
 * @since 020-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StoreOrderRefundRequest对象", description="订单退款")
public class StoreOrderRefundRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单id")
    @Min(value = 1, message = "请选择订单")
    private Integer orderId;

    @ApiModelProperty(value = "退款金额")
    @DecimalMin(value = "0.00", message = "退款金额不能少于0.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "status 1 = 直接退款, 2 = 退款后,返回原状态", allowableValues = "range[1,2]")
    @Range(min = 1, max = 2, message = "请选择退款状态 1 = 直接退款, 2 = 退款后,返回原状态")
    private int type;

}
