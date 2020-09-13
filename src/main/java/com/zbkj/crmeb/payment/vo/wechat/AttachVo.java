package com.zbkj.crmeb.payment.vo.wechat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AttachVo对象", description="支付附加对象")
public class AttachVo {

    public AttachVo() {
    }

    public AttachVo(String type, Integer userId) {
        this.type = type;
        this.userId = userId;
    }

    @ApiModelProperty(value = "业务类型， 订单 = order， 充值 = recharge", required = true)
    private String type = "order";

    @ApiModelProperty(value = "用户id", required = true)
    private Integer userId;

}
