package com.zbkj.crmeb.store.request;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author stivepeim
 * @title: StoreOrderSearchRequest
 * @projectName crmeb
 * @description: TODO
 * @date 2020/5/2816:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_order")
@ApiModel(value="StoreOrderSearchRequest对象", description="订单表")
public class StoreOrderSearchRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "创建时间区间")
    private String dateLimit;

    @ApiModelProperty(value = "订单状态（all 总数； 未支付 unPaid； 未发货 notShipped；待收货 spike；待评价 bargain；已完成 complete；待核销 toBeWrittenOff；退款中:refunding；已退款:refunded；已删除:deleted")
    private String status;

    @ApiModelProperty(value = "是否删除")
    private Boolean isDel;
}
