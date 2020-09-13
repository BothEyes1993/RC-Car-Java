package com.zbkj.crmeb.store.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 订单状态数量
 * </p>
 *
 * @author Mr.Zhang
 * @since 2020-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="StoreOrderCountItemResponse对象", description="订单状态数量")
public class StoreOrderCountItemResponse implements Serializable {
    public StoreOrderCountItemResponse(){}
    public StoreOrderCountItemResponse(Integer all, Integer unPaid, Integer notShipped, Integer spike, Integer bargain, Integer complete, Integer toBeWrittenOff, Integer refunding, Integer refunded, Integer deleted) {
        this.all = all;
        this.unPaid = unPaid;
        this.notShipped = notShipped;
        this.spike = spike;
        this.bargain = bargain;
        this.complete = complete;
        this.toBeWrittenOff = toBeWrittenOff;
        this.refunding = refunding;
        this.refunded = refunded;
        this.deleted = deleted;
    }

    @ApiModelProperty(value = "总数")
    private Integer all;

    @ApiModelProperty(value = "未支付")
    private Integer unPaid;

    @ApiModelProperty(value = "未发货")
    private Integer notShipped;

    @ApiModelProperty(value = "待收货")
    private Integer spike;

    @ApiModelProperty(value = "待评价")
    private Integer bargain;

    @ApiModelProperty(value = "交易完成")
    private Integer complete;

    @ApiModelProperty(value = "待核销")
    private Integer toBeWrittenOff;

    @ApiModelProperty(value = "退款中")
    private Integer refunding;

    @ApiModelProperty(value = "已退款")
    private Integer refunded;

    @ApiModelProperty(value = "已删除")
    private Integer deleted;
}
