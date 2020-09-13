package com.zbkj.crmeb.front.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author stivepeim
 * @title: StoreCartRequest
 * @projectName crmeb
 * @description: 添加购物车参数
 * @date 2020/7/4
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderRefundApplyRequest对象", description="订单申请退款")
public class OrderRefundApplyRequest {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "退款原因", required = true)
    @NotNull(message = "退款原因必须填写")
    private String text;

    @ApiModelProperty(value = "订单id", required = true)
    @NotBlank(message = "请选择订单")
    private Integer id;

    @ApiModelProperty(value = "退款凭证(多个图片请用,(英文逗号)隔开)")
    @JsonProperty("refund_reason_wap_img")
    private String reasonImage;

    @ApiModelProperty(value = "备注说明")
    @JsonProperty("refund_reason_wap_explain")
    private String explain;
}
